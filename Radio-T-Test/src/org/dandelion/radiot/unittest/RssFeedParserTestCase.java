package org.dandelion.radiot.unittest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.dandelion.radiot.rss.RssEnclosure;
import org.dandelion.radiot.rss.RssFeedParser;
import org.dandelion.radiot.rss.RssFeedParser.FeedItemListener;
import org.dandelion.radiot.rss.RssItem;

import android.net.Uri;

import junit.framework.TestCase;

public class RssFeedParserTestCase extends TestCase {

	private String feedContent;
	private RssFeedParser rssParser;
	private ArrayList<RssItem> items;
	private RssItem firstParsedItem;
	private boolean streamClosed;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		feedContent = "";
		items = new ArrayList<RssItem>();
	}

	public void testExtractingSimpleItemFields() throws Exception {
		newFeedItem("<title>RSS Item title</title>"
				+ "<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>"
				+ "<description><![CDATA[Description]]></description>"
				+ "<content:encoded><![CDATA[Encoded content]]></content:encoded>");
		parseRssFeed();
		assertEquals("RSS Item title", firstParsedItem.title);
		assertEquals("Sun, 13 Jun 2010 01:37:22 +0000", firstParsedItem.pubDate);
		assertEquals("Description", firstParsedItem.description);
		assertEquals("Encoded content", firstParsedItem.encodedContent);
	}

	public void testExtractingCategories() throws Exception {
		newFeedItem("<category>Tag1</category><category>Tag2</category>");
		parseRssFeed();
		assertTrue(firstParsedItem.hasCategory("Tag1"));
		assertTrue(firstParsedItem.hasCategory("Tag2"));
	}

	public void testExtractingEnclosureDetails() throws Exception {
		newFeedItem("<enclosure url=\"http://enclosure-url\" type=\"audio/mpeg\"/>");

		parseRssFeed();
		assertEquals(1, firstParsedItem.enclosures.size());

		RssEnclosure enclosure = firstParsedItem.enclosures.get(0);
		assertEquals("audio/mpeg", enclosure.type);
		assertEquals("http://enclosure-url", enclosure.url);
	}

	public void testExtractingAllEnclosures() throws Exception {
		newFeedItem("<enclosure url=\"http://enclosure-url\" type=\"audio/mpeg\"/>"
				+ "<enclosure url=\"http://enclosure-url\" type=\"audio/mpeg\"/>");

		parseRssFeed();
		assertEquals(2, firstParsedItem.enclosures.size());
	}

	public void testEnsureStreamIsClosedAfterParsing() throws Exception {
		streamClosed = false;
		parseRssFeed();
		assertTrue(streamClosed);
	}

	public void testHandleParsingErrors() throws Exception {
		newFeedItem("<number>102");
		try {
			parseRssFeed();
		} catch (Exception e) {
			return;
		}
		fail("Should have raised the exception");
	}

	private void newFeedItem(String itemContent) {
		feedContent = feedContent + "<item>" + itemContent + "</item>";
	}

	private void parseRssFeed() throws Exception {
		rssParser = new RssFeedParser(new ByteArrayInputStream(
				getCompleteFeed().getBytes()) {
			@Override
			public void close() throws IOException {
				super.close();
				streamClosed = true;
			}
		});

		rssParser.setItemListener(new FeedItemListener() {
			public void item(RssItem item) {
				items.add(item);
			}
		});

		rssParser.parse();

		if (!items.isEmpty()) {
			firstParsedItem = items.get(0);
		}
	}

	private String getCompleteFeed() {
		return "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"><channel>"
				+ feedContent + "</channel></rss>";
	}
}
