package org.dandelion.radiot.rss;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

public class RssFeedParser {
	public interface ParserListener {
		void onItemParsed(RssItem item);
	}

	protected ParserListener listener;
	public RssItem currentItem = new RssItem();
	private IFeedSource feedSource;

	public static RssFeedParser withRemoteFeed(final String feedUrl) {
		return new RssFeedParser(new RemoteFeedSource(feedUrl));
	}

	public RssFeedParser(IFeedSource source) {
		feedSource = source;
	}

	public void parse() throws IOException, SAXException {
		Xml.parse(feedSource.openFeedStream(), Xml.Encoding.UTF_8,
				getContentHandler());
	}

	private ContentHandler getContentHandler() {
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				if (null != listener) {
					listener.onItemParsed(currentItem);
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

	public void setItemListener(ParserListener listener) {
		this.listener = listener;
	}
}