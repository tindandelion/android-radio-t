package org.dandelion.radiot.unittest;

import junit.framework.TestCase;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.RssFeedProvider;

import java.io.IOException;

public class RssFeedProviderTest extends TestCase {
    public static final String ADDRESS = "";
    private RssFeedProvider provider;
    private PodcastList parsedItems;
	private PodcastItem firstParsedItem;
    private FakeRssClient rssClient;


    @Override
	protected void setUp() throws Exception {
		super.setUp();
        rssClient = new FakeRssClient();
        provider = new RssFeedProvider(ADDRESS, rssClient);
	}

    public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
        rssClient.newFeedItem("");
        rssClient.newFeedItem("");

        parseRssFeed();
		assertEquals(2, parsedItems.size());
		assertNotNull(parsedItems.first());
	}

	public void testExtractingPodcastTitle() throws Exception {
        rssClient.newFeedItem("<title>Radio 192</title>");
        parseRssFeed();
        assertEquals("Radio 192", firstParsedItem.title);
	}

	public void testExtractPodcastDate() throws Exception {
        rssClient.newFeedItem("<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>");
        parseRssFeed();
		assertEquals("Sun, 13 Jun 2010 01:37:22 +0000", firstParsedItem.pubDate);
	}

	public void testExtractShowNotes() throws Exception {
        rssClient.newFeedItem("<itunes:summary>Show notes</itunes:summary>");
        parseRssFeed();
		assertEquals("Show notes", firstParsedItem.showNotes);
	}

	public void testExtractPodcastLink() throws Exception {
        rssClient.newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>");
        parseRssFeed();
        assertEquals("http://podcast-link", firstParsedItem.audioUri);
	}

	public void testSkipNonAudioEnsclosures() throws Exception {
        rssClient.newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>"
				+ "<enclosure url=\"http://yet-another-link\" type=\"text/xml\"/>");

        parseRssFeed();

        assertEquals("http://podcast-link", firstParsedItem.audioUri);
	}

    public void testExtractThumbnailUrl() throws Exception {
        rssClient.newFeedItem("<description>" +
                "&lt;p&gt;&lt;img src=\"http://www.radio-t.com/images/radio-t/rt302.jpg\" alt=\"\" /&gt;&lt;/p&gt;\n" +
                "</description>");
        parseRssFeed();
        assertEquals("http://www.radio-t.com/images/radio-t/rt302.jpg", firstParsedItem.thumbnailUrl);
    }

	public void testHandleParsingErrors() throws Exception {
        rssClient.newFeedItem("<number>102");
        try {
			parseRssFeed();
		} catch (Exception e) {
			return;
		}
		fail("Should have raised the exception");
	}

    private void parseRssFeed() throws Exception {
		parsedItems = provider.retrieve();
        firstParsedItem = parsedItems.first();
	}

    private static class FakeRssClient implements HttpClient {
        private String feedContent = "";

        @Override
        public String getStringContent(String url) throws IOException {
            return getCompleteFeed();
        }

        @Override
        public byte[] getByteContent(String url) throws IOException {
            throw new RuntimeException("Should not implement");
        }

        public void newFeedItem(String itemContent) {
            feedContent = feedContent + "<item>" + itemContent + "</item>";
        }

        public String getCompleteFeed() {
            return "<rss xmlns:media=\"http://search.yahoo.com/mrss/\" " +
                    "xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"><channel>"
                    + feedContent + "</channel></rss>";
        }
    }
}
