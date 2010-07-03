package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

public class RssFeedModel implements PodcastList.IModel {
	
	public static class UrlFeedSource implements PodcastList.IFeedSource {
		private String address;
		public UrlFeedSource(String url) {
			this.address = url;
		}
		public InputStream openContentStream() throws IOException {
			URL url = new URL(address);
			return url.openStream();
		} 
	}

	private ArrayList<PodcastItem> items;
	private PodcastList.IFeedSource feedSource;

	public RssFeedModel(PodcastList.IFeedSource source) {
		feedSource = source;
	}
	
	public List<PodcastItem> retrievePodcasts() throws Exception {
		items = new ArrayList<PodcastItem>();
		Xml.parse(feedSource.openContentStream(), Xml.Encoding.UTF_8,
				getContentHandler());
		return items;
	}

	private ContentHandler getContentHandler() {
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");
		final PodcastItem currentItem = new PodcastItem();

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				items.add(currentItem.copy());
			}
		});

		item.getChild("title").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.extractPodcastNumber(body);
					}
				});

		item.getChild("pubDate").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.extractPubDate(body);
					}
				});

		item.getChild("description").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.extractShowNotes(body);
					}
				});

		item.getChild("enclosure").setStartElementListener(
				new StartElementListener() {
					public void start(Attributes attributes) {
						if (isAudioEnclosure(attributes)) {
							currentItem.extractAudioUri(attributes
									.getValue("url"));
						}
					}

					private boolean isAudioEnclosure(Attributes attributes) {
						return attributes.getValue("type").equals("audio/mpeg");
					}
				});

		return root.getContentHandler();
	}

}
