package org.dandelion.radiot.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.RssFeedParser;
import org.xml.sax.SAXException;

import android.test.InstrumentationTestCase;

public class RssFeedParserTestCase extends InstrumentationTestCase {

	private RssFeedParser provider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new RssFeedParser();
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		List<PodcastItem> items = parseRssFeed(twoItemsXml());

		assertEquals(2, items.size());
		assertNotNull(items.get(0));
	}

	private List<PodcastItem> parseRssFeed(String contents)
			throws SAXException, IOException {
		InputStream stream = new ByteArrayInputStream(contents.getBytes());
		return provider.readRssFeed(stream);
	}

	private String twoItemsXml() {
		return
		"<rss><channel>" + 
		"<item></item><item></item>" + 
		"</channel></rss>";
	}
}
