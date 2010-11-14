package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

class RssEnclosure {
	public String url = "";
	public String type = "";

	public RssEnclosure(String type, String url) {
		this.type = type;
		this.url = url;
	}

	public boolean hasType(String type) {
		return this.type.equals(type);
	}
}

class RssItem {
	public String title = "";
	public String pubDate = "";
	public String description = "";
	public String encodedContent = "";
	public ArrayList<String> categories = new ArrayList<String>();
	public ArrayList<RssEnclosure> enclosures = new ArrayList<RssEnclosure>();

	public void addCategory(String value) {
		categories.add(value);
	}

	public void addEnclosure(RssEnclosure enclosure) {
		enclosures.add(enclosure);
	}

	public List<RssEnclosure> getEnclosures(String type) {
		List<RssEnclosure> result = new ArrayList<RssEnclosure>();
		for (RssEnclosure item : enclosures) {
			if (item.hasType(type)) {
				result.add(item);
			}
		}

		return result;
	}
}

class RssFeedParser {
	public interface FeedItemListener {
		void item(RssItem item);
	}

	private InputStream contentStream;
	private FeedItemListener itemListener;
	public RssItem currentItem = new RssItem();

	public RssFeedParser(InputStream contentStream) {
		this.contentStream = contentStream;
	}

	public void parse() throws IOException, SAXException {
		Xml.parse(this.contentStream, Xml.Encoding.UTF_8, getContentHandler());
	}

	private ContentHandler getContentHandler() {
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				if (null != itemListener) {
					itemListener.item(currentItem);
				}
				currentItem = new RssItem();
			}
		});

		item.getChild("title").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.title = body;
					}
				});

		item.getChild("pubDate").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.pubDate = body;
					}
				});

		item.getChild("description").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.description = body;
					}
				});

		item.getChild("http://purl.org/rss/1.0/modules/content/", "encoded")
				.setEndTextElementListener(new EndTextElementListener() {
					public void end(String body) {
						currentItem.encodedContent = body;
					}
				});

		item.getChild("category").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.addCategory(body);
					}
				});

		item.getChild("enclosure").setStartElementListener(
				new StartElementListener() {
					public void start(Attributes attributes) {
						RssEnclosure enclosure = new RssEnclosure(attributes
								.getValue("type"), attributes.getValue("url"));
						currentItem.addEnclosure(enclosure);
					}
				});

		return root.getContentHandler();
	}

	public void setItemListener(FeedItemListener listener) {
		this.itemListener = listener;
	}
}

public class RssFeedModel implements PodcastList.IModel {
	private ArrayList<PodcastItem> items;
	private String address;

	public RssFeedModel(String url) {
		this.address = url;
	}

	public List<PodcastItem> retrievePodcasts() throws Exception {
		items = new ArrayList<PodcastItem>();
		RssFeedParser feed = new RssFeedParser(openContentStream());

		feed.setItemListener(new RssFeedParser.FeedItemListener() {
			public void item(RssItem item) {
				PodcastItem currentItem = new PodcastItem();
				currentItem.extractPodcastNumber(item.title);
				currentItem.extractPubDate(item.pubDate);
				currentItem.extractShowNotes(item.description);
				currentItem.extractImageUrl(item.encodedContent);

				for (String category : item.categories) {
					currentItem.addTag(category);
				}

				for (RssEnclosure enclosure : item.getEnclosures("audio/mpeg")) {
					currentItem.extractAudioUri(enclosure.url);
				}
				items.add(currentItem);
				currentItem = new PodcastItem();
			}
		});

		feed.parse();
		return items;
	}

	protected InputStream openContentStream() throws IOException {
		URL url = new URL(address);
		return url.openStream();
	}

	protected InputStream openImageStream(String address) {
		try {
			URL url = new URL(address);
			return url.openStream();
		} catch (Exception ex) {
			return null;
		}
	}

	public Bitmap loadPodcastImage(PodcastItem item) {
		return BitmapFactory.decodeStream(openImageStream(item.getImageUrl()));
	}
}
