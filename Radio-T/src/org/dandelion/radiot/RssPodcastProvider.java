package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.PodcastListActivity.PodcastListAdapter;

import android.content.Context;
import android.util.Log;

public class RssPodcastProvider implements IPodcastProvider {
	
	private static final String RSS_FILENAME = "podcast_rss.xml";
	private Context context;

	public RssPodcastProvider(Context context) {
		this.context = context;
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
			Log.e("RadioT", "Error while parsing RSS", e);
		} 
	}

	private InputStream openContentStream() throws IOException {
		return context.getAssets().open(RSS_FILENAME);
	}

}
