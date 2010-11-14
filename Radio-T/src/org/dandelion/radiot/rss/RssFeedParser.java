package org.dandelion.radiot.rss;


import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

public class RssFeedParser extends AbstractFeedParser implements IFeedParser {
	public RssItem currentItem = new RssItem();

	public static IFeedParser withRemoteFeed(final String feedUrl) {
		return new RssFeedParser(new RemoteFeedSource(feedUrl));
	}

	public RssFeedParser(IFeedSource source) {
		super(source);
	}

	@Override
	protected ContentHandler getContentHandler() {
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				fireNewItem(currentItem);
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

}