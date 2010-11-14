package org.dandelion.radiot.unittest;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.dandelion.radiot.helpers.FakeFeedSource;
import org.dandelion.radiot.rss.IFeedParser.ParserListener;
import org.dandelion.radiot.rss.RssEnclosure;
import org.dandelion.radiot.rss.RssFeedParser;
import org.dandelion.radiot.rss.RssItem;

class RssFeedSource extends FakeFeedSource {
	public String getCompleteFeed() {
		return "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"><channel>"
				+ getFeedContent() + "</channel></rss>";
	};

}

public class RssFeedParserTestCase extends TestCase {
	private RssFeedParser parser;
	private ArrayList<RssItem> items;
	private RssItem firstParsedItem;
	private FakeFeedSource feedSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		feedSource = new RssFeedSource();
		parser = new RssFeedParser(feedSource);
		parser.setListener(new ParserListener() {
			public void onItemParsed(RssItem item) {
				items.add(item);
			}
		});
		items = new ArrayList<RssItem>();
	}

	public void testExtractingSimpleItemFields() throws Exception {
		setFeedItem("<title>RSS Item title</title>"
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
		setFeedItem("<category>Tag1</category><category>Tag2</category>");
		parseRssFeed();
		assertTrue(firstParsedItem.hasCategory("Tag1"));
		assertTrue(firstParsedItem.hasCategory("Tag2"));
	}

	public void testExtractingEnclosureDetails() throws Exception {
		setFeedItem("<enclosure url=\"http://enclosure-url\" type=\"audio/mpeg\"/>");

		parseRssFeed();
		assertEquals(1, firstParsedItem.enclosures.size());

		RssEnclosure enclosure = firstParsedItem.enclosures.get(0);
		assertEquals("audio/mpeg", enclosure.type);
		assertEquals("http://enclosure-url", enclosure.url);
	}

	public void testExtractingAllEnclosures() throws Exception {
		setFeedItem("<enclosure url=\"http://enclosure-url\" type=\"audio/mpeg\"/>"
				+ "<enclosure url=\"http://enclosure-url\" type=\"audio/mpeg\"/>");

		parseRssFeed();
		assertEquals(2, firstParsedItem.enclosures.size());
	}

	public void testHandleParsingErrors() throws Exception {
		setFeedItem("<number>102");
		try {
			parseRssFeed();
		} catch (Exception e) {
			return;
		}
		fail("Should have raised the exception");
	}

	private void setFeedItem(String itemContent) {
		feedSource.setFeedContent("<item>" + itemContent + "</item>");
	}

	private void parseRssFeed() throws Exception {
		parser.parse();
		if (!items.isEmpty()) {
			firstParsedItem = items.get(0);
		}
	}
}
