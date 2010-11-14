package org.dandelion.radiot.unittest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.dandelion.radiot.rss.IFeedSource;
import org.dandelion.radiot.rss.RssEnclosure;
import org.dandelion.radiot.rss.RssFeedParser;
import org.dandelion.radiot.rss.RssFeedParser.ParserListener;
import org.dandelion.radiot.rss.RssItem;

public class RssFeedParserTestCase extends TestCase {
	private String feedContent;
	private RssFeedParser parser;
	private ArrayList<RssItem> items;
	private RssItem firstParsedItem;
	private IFeedSource feedSource = new IFeedSource() {
		public InputStream openFeedStream() throws IOException {
			return new ByteArrayInputStream(getCompleteFeed().getBytes());
		}

		private String getCompleteFeed() {
			return "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"><channel>"
					+ feedContent + "</channel></rss>";
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		feedContent = "";
		parser = new RssFeedParser(feedSource);
		parser.setItemListener(new ParserListener() {
			public void onItemParsed(RssItem item) {
				items.add(item);
			}
		});
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
		parser.parse();
		if (!items.isEmpty()) {
			firstParsedItem = items.get(0);
		}
	}
}
