package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;

import android.util.Log;

public class RssPodcastProvider implements IPodcastProvider {
	
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

	protected InputStream openContentStream() throws IOException {
		return null;
	}

	private List<PodcastItem> retrievePodcasts() {
		RssFeedParser feedParser = new RssFeedParser();
		try {
			InputStream contentStream = openContentStream();
			List<PodcastItem> items = feedParser.readRssFeed(contentStream);
			contentStream.close();
			return items;
		} catch (Exception e) {
			Log.e("RadioT", "Error loading podcast RSS", e);
			return new ArrayList<PodcastItem>();
		}
	}

	public void refreshPodcasts(PodcastListActivity activity) {
		activity.updatePodcasts(retrievePodcasts());
	}
}
