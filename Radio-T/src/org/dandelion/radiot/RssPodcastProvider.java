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

		private List<PodcastInfo> podcastItems = new ArrayList<PodcastInfo>();

		public List<PodcastInfo> getPodcastItems() {
			return podcastItems;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equalsIgnoreCase("item")) {
				podcastItems.add(new PodcastInfo("1"));
			}
		}

	}

	public List<PodcastInfo> readRssFeed(InputStream source) throws ParserConfigurationException, SAXException, IOException {
		SAXParser parser = createParser();
		RssHandler handler = new RssHandler();
		parser.parse(source, handler);
		return handler.getPodcastItems();
	}

	private SAXParser createParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		return factory.newSAXParser();
	}
	
}
