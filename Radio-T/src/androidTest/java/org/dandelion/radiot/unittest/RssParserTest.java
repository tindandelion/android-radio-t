package org.dandelion.radiot.unittest;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.rss.RssParser;
import org.xml.sax.SAXException;

public class RssParserTest extends TestCase {
    private RssBuilder rssBuilder = new RssBuilder();
    private RssParser parser = new RssParser(new TestNotesExtractor());


    public void test_parseAllItems() throws Exception {
        rssBuilder.newFeedItem("");
        rssBuilder.newFeedItem("");

        PodcastList items = parseRssFeed();
		assertEquals(2, items.size());
		assertNotNull(items.first());
	}

	public void test_extractTitle() throws Exception {
        rssBuilder.newFeedItem("<title>Radio 192</title>");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("Radio 192", item.title);
	}

	public void test_extractDate() throws Exception {
        rssBuilder.newFeedItem("<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("Sun, 13 Jun 2010 01:37:22 +0000", item.pubDate);
	}

	public void test_extractShowNotes_invokesNotesExtractor() throws Exception {
        rssBuilder.newFeedItem("<itunes:summary>Show notes</itunes:summary>");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("test - Show notes", item.showNotes);
	}

	public void test_extractDownloadLink_typeMp3() throws Exception {
        rssBuilder.newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mp3\"/>");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("http://podcast-link", item.audioUri);
	}

    public void test_extractDownloadLink_typeMpeg() throws Exception {
        rssBuilder.newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("http://podcast-link", item.audioUri);
    }


    public void test_extractDownloadLink_skipNonAudioEnclosures() throws Exception {
        rssBuilder.newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mp3\"/>"
				+ "<enclosure url=\"http://yet-another-link\" type=\"text/xml\"/>");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("http://podcast-link", item.audioUri);
	}

    public void test_extractDownloadLink_skipEnclosuresWithoutType() throws Exception {
        rssBuilder.newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mp3\"/>"
                + "<enclosure url=\"http://yet-another-link\" />");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("http://podcast-link", item.audioUri);
    }

    public void test_extractThumbnailUrl_fromITunesTag() throws Exception {
        rssBuilder.newFeedItem("<itunes:image href=\"https://radio-t.com/images/radio-t/rt526.jpg\" />");

        PodcastItem item = firstItem(parseRssFeed());
        assertEquals("https://radio-t.com/images/radio-t/rt526.jpg", item.thumbnailUrl);
    }

    public void test_throwError_whenParseErrorHappens() throws Exception {
        rssBuilder.newFeedItem("<number>102");
        try {
			parseRssFeed();
		} catch (SAXException e) {
			return;
		}
		fail("Should have raised the exception");
	}

    private PodcastList parseRssFeed() throws Exception {
        String content = rssBuilder.getCompleteFeed();
        return parser.parse(content);
	}

    private PodcastItem firstItem(PodcastList items) {
        return items.first();
    }

    private static class RssBuilder {
        private String feedContent = "";

        void newFeedItem(String itemContent) {
            feedContent = feedContent + "<item>" + itemContent + "</item>";
        }

        String getCompleteFeed() {
            return "<rss xmlns:media=\"http://search.yahoo.com/mrss/\" " +
                    "xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"><channel>"
                    + feedContent + "</channel></rss>";
        }
    }

    private static class TestNotesExtractor implements RssParser.NotesExtractor {

        @Override
        public String extract(String text) {
            return "test - " + text;
        }
    }
}
