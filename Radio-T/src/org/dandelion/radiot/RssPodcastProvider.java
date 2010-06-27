package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.RssFeedModel.IFeedSource;

import android.util.Log;

public class RssPodcastProvider implements IPodcastProvider, IFeedSource {
	
	public InputStream openContentStream() throws IOException {
		return null;
	}

	protected List<PodcastItem> retrievePodcasts() {
		RssFeedModel feedParser = new RssFeedModel(this);
		try {
			InputStream contentStream = openContentStream();
			List<PodcastItem> items = feedParser.retrievePodcasts();
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
