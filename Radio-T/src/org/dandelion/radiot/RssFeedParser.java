package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class RssFeedParser {
	private ArrayList<PodcastItem> items;

	public List<PodcastItem> readRssFeed(InputStream contentStream)
			throws ParserConfigurationException, SAXException, IOException {
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
		
		return root.getContentHandler();
	}
}
