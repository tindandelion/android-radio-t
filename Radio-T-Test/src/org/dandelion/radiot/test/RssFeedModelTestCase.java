package org.dandelion.radiot.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.RssFeedModel;

import android.net.Uri;

public class RssFeedModelTestCase extends TestCase {

	private RssFeedModel model;
	private String feedContent;
	private List<PodcastItem> parsedItems;
	private PodcastItem firstParsedItem;
	protected boolean streamClosed;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		model = createTestModel();
		feedContent = "";
	}

	protected RssFeedModel createTestModel() {
		return new RssFeedModel(null) {
			protected InputStream openContentStream() throws IOException {
				return new ByteArrayInputStream(getCompleteFeed().getBytes()) {
					@Override
					public void close() throws IOException {
						super.close();
						streamClosed = true;
					}
				};
			};
		};
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		newFeedItem("");
		newFeedItem("");

		parseRssFeed();

		assertEquals(2, parsedItems.size());
		assertNotNull(parsedItems.get(0));
	}

	public void testExtractingPodcastNumber() throws Exception {
		newFeedItem("<title>Radio 192</title>");

		parseRssFeed();

		assertEquals("#192", firstParsedItem.getNumber());
	}

	public void testExtractPodcastDate() throws Exception {
		newFeedItem("<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>");

		parseRssFeed();

		assertEquals("13.06.2010", firstParsedItem.getPubDate());
	}

	public void testExtractShowNotes() throws Exception {
		newFeedItem("<description><![CDATA[Show notes]]></description>");
		parseRssFeed();
		assertEquals("Show notes", firstParsedItem.getShowNotes());
	}

	public void testExtractPodcastLink() throws Exception {
		newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>");
		parseRssFeed();
		assertEquals(Uri.parse("http://podcast-link"),
				firstParsedItem.getAudioUri());
	}

	public void testSkipNonAudioEnsclosures() throws Exception {
		newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>"
				+ "<enclosure url=\"http://yet-another-link\" type=\"text/xml\"/>");

		parseRssFeed();

		assertEquals(Uri.parse("http://podcast-link"),
				firstParsedItem.getAudioUri());
	}

	public void testEnsureStreamIsClosed() throws Exception {
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

	public void testExtractingTags() throws Exception {
		newFeedItem("<category>Tag1</category><category>Tag2</category>");
		parseRssFeed();
		assertTrue(firstParsedItem.hasTag("Tag1"));
		assertTrue(firstParsedItem.hasTag("Tag2"));
	}

	private void newFeedItem(String itemContent) {
		feedContent = feedContent + "<item>" + itemContent + "</item>";
	}

	private void parseRssFeed() throws Exception {
		parsedItems = model.retrievePodcasts();
		if (!parsedItems.isEmpty()) {
			firstParsedItem = parsedItems.get(0);
		}
	}

	private String getCompleteFeed() {
		return "<rss><channel>" + feedContent + "</channel></rss>";
	}
}
