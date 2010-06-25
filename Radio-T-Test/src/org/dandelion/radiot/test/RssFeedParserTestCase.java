package org.dandelion.radiot.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.RssFeedParser;
import org.xml.sax.SAXException;

public class RssFeedParserTestCase extends TestCase {

	private RssFeedParser provider;
	private String feedContent;
	private List<PodcastItem> items;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new RssFeedParser();
		feedContent = "";
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		newFeedItem("");
		newFeedItem("");
		
		items = parseRssFeed();

		assertEquals(2, items.size());
		assertNotNull(items.get(0));
	}

	public void testExtractingPodcastNumber() throws Exception {
		newFeedItem("<number>Radio 192</number>");
		
		items = parseRssFeed();
		PodcastItem item = items.get(0);
		
		assertEquals(192, item.getNumber());
	}
	
	public void testExtractPodcastDate() throws Exception {
		newFeedItem("<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>");
		
		items = parseRssFeed();
		PodcastItem item = items.get(0);
		String strDate = new SimpleDateFormat("dd.MM.yyyy").format(item.getPubDate());
		
		assertEquals("13.06.2010", strDate);
	}
	

	private void newFeedItem(String itemContent) {
		feedContent = feedContent + "<item>" + itemContent + "</item>";
	}

	private List<PodcastItem> parseRssFeed()
			throws SAXException, IOException {
		InputStream stream = new ByteArrayInputStream(getCompleteFeed().getBytes());
		return provider.readRssFeed(stream);
	}

	private String getCompleteFeed() {
		return "<rss><channel>" + feedContent + "</channel></rss>";
	}
}
