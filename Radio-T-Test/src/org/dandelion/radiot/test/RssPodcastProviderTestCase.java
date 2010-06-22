package org.dandelion.radiot.test;

import java.io.InputStream;
import java.util.List;

import org.dandelion.radiot.PodcastInfo;
import org.dandelion.radiot.RssPodcastProvider;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class RssPodcastProviderTestCase extends InstrumentationTestCase {

	private RssPodcastProvider provider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new RssPodcastProvider();
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		List<PodcastInfo> items = parseRssFeed();

		assertEquals(2, items.size());
	}

	private List<PodcastInfo> parseRssFeed() throws Exception {
		return provider.readRssFeed(testRssStream());
	}

	public void testReadingPodcastNumber() throws Exception {
		List<PodcastInfo> items = parseRssFeed();
		
		PodcastInfo item = items.get(0);
		
		assertEquals("#191", item.getNumber());
	}

	private InputStream testRssStream() throws Exception {
		AssetManager assets = getInstrumentation().getContext().getAssets();
		return assets.open("test_rss.xml");
	}
}
