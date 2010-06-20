package org.dandelion.radiot.test;

import java.net.URL;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastInfo;
import org.dandelion.radiot.RssPodcastProvider;

import android.net.Uri;

public class RssPodcastProviderTestCase extends TestCase {
	
	public void testParseSingleItem() throws Exception {
		RssPodcastProvider provider = new RssPodcastProvider(oneItemRssFeed());
		
		ArrayList<PodcastInfo> list = new ArrayList<PodcastInfo>();
		provider.parseFeed(list);
		
		assertEquals(1, list.size());
	}
	
	public void testAsset() throws Exception {
		URL url = new URL("file:///android_asset/one_item_feed.xml");
		url.openConnection().getContent();
	}

	private Uri oneItemRssFeed() {
		return Uri.parse("file:///android_asset/one_item_feed.xml");
	}
}
