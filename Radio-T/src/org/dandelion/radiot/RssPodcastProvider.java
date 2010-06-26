package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.PodcastListActivity.PodcastListAdapter;

import android.content.res.AssetManager;
import android.util.Log;

public class RssPodcastProvider implements IPodcastProvider {
	
	public static class LocalRssProvider extends RssPodcastProvider {
		private static final String RSS_FILENAME = "podcast_rss.xml";

		private AssetManager assets;
		
		public LocalRssProvider(AssetManager assets) {
			this.assets = assets;
		}
		
		@Override
		protected InputStream openContentStream() throws IOException {
			return assets.open(RSS_FILENAME);
		}
	}
	
	public static class RemoteRssProvider extends RssPodcastProvider {
		
		private String feedUrl;

		public RemoteRssProvider(String feedUrl) {
			this.feedUrl = feedUrl;
		}
		
		@Override
		protected InputStream openContentStream() throws IOException {
			URL url = new URL(feedUrl);
			return url.openStream();
		} 
	}

	public void retrievePodcasts(PodcastListAdapter listAdapter) {
		RssFeedParser feedParser = new RssFeedParser();
		try {
			InputStream contentStream = openContentStream();
			List<PodcastItem> items = feedParser.readRssFeed(contentStream);
			for (PodcastItem podcastItem : items) {
				listAdapter.add(podcastItem);
			}
			contentStream.close();
		} catch (Exception e) {
			Log.e("RadioT", "Error loading podcast RSS", e);
		} 
	}

	protected InputStream openContentStream() throws IOException {
		return null;
	}

	public List<PodcastItem> getPodcastList() {
		// TODO Auto-generated method stub
		return null;
	}
}
