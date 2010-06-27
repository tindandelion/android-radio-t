package org.dandelion.radiot;

import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.PodcastList.IPodcastListModel;
import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.RssFeedModel.IFeedSource;

import android.util.Log;

public class RssPodcastProvider implements IPodcastProvider {
	private RssFeedModel model;
	
	public RssPodcastProvider(IFeedSource feedSource) {
		model = new RssFeedModel(feedSource);
	}

	protected List<PodcastItem> retrievePodcasts() {
		try {
			return model.retrievePodcasts();
		} catch (Exception e) {
			Log.e("RadioT", "Error loading podcast RSS", e);
			return new ArrayList<PodcastItem>();
		}
	}

	public void refreshPodcasts(PodcastListActivity activity) {
		activity.updatePodcasts(retrievePodcasts());
	}

	public IPodcastListModel getModel() {
		return model;
	}
}
