package org.dandelion.radiot.test;

import java.io.InputStream;
import java.util.List;

import org.dandelion.radiot.PodcastInfo;
import org.dandelion.radiot.RssPodcastProvider;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class RssPodcastProviderTestCase extends InstrumentationTestCase {

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		RssPodcastProvider provider = new RssPodcastProvider();
		
		List<PodcastInfo> items = provider.readRssFeed(testRssStream());

		assertEquals(2, items.size());
	}

	private InputStream testRssStream() throws Exception {
		AssetManager assets = getInstrumentation().getContext().getAssets();
		return assets.open("test_rss.xml");
	}
}
