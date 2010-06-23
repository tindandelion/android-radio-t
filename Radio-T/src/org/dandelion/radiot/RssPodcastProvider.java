package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class RssPodcastProvider {

	public class RssHandler extends DefaultHandler {

		private static final String ITEM = "item";

		private List<PodcastItem> items;
		private PodcastItem currentItem;

		public List<PodcastItem> getPodcastItems() {
			return items;
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			items = new ArrayList<PodcastItem>();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equalsIgnoreCase(ITEM)) {
				currentItem = new PodcastItem();
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			
			if (currentItem == null) {
				return;
			}
			
			if (localName.equalsIgnoreCase(ITEM)) {
				items.add(currentItem);
			}
		}

	}

	public List<PodcastItem> readRssFeed(InputStream contentStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParser parser = createParser();
		RssHandler handler = new RssHandler();
		parser.parse(contentStream, handler);
		return handler.getPodcastItems();
	}

	private SAXParser createParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		return factory.newSAXParser();
	}
	
}
