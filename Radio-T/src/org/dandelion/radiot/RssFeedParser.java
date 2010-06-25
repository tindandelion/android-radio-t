package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class RssFeedParser {
	private ArrayList<PodcastItem> items;

	public List<PodcastItem> readRssFeed(InputStream contentStream)
			throws SAXException, IOException {
		items = new ArrayList<PodcastItem>();
		Xml.parse(contentStream, Xml.Encoding.UTF_8, getContentHandler());
		return items;
	}

	private ContentHandler getContentHandler() {
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");
		final PodcastItem current = new PodcastItem();
		
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				items.add(current.copy());
			}
		});
		
		item.getChild("number").setEndTextElementListener(new EndTextElementListener() {
			public void end(String value) {
				current.extractPodcastNumber(value);
			}
		});
		
		return root.getContentHandler();
	}
}
