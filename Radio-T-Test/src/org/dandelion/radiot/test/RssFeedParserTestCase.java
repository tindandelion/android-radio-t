package org.dandelion.radiot.test;

import java.io.InputStream;
import java.util.List;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.RssFeedParser;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class RssFeedParserTestCase extends InstrumentationTestCase {

	private RssFeedParser provider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new RssFeedParser();
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		List<PodcastItem> items = parseRssFeed();

		assertEquals(2, items.size());
	}

	private List<PodcastItem> parseRssFeed() throws Exception {
		return provider.readRssFeed(testRssStream());
	}

	private InputStream testRssStream() throws Exception {
		AssetManager assets = getInstrumentation().getContext().getAssets();
		return assets.open("test_rss.xml");
	}
}
