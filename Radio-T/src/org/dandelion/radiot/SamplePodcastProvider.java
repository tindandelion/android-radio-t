package org.dandelion.radiot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.PodcastListActivity.PodcastListAdapter;

public class SamplePodcastProvider implements IPodcastProvider {

	private List<PodcastItem> podcasts = new ArrayList<PodcastItem>();

	public void retrievePodcasts(PodcastListAdapter listAdapter) {
		for (PodcastItem item : getPodcastList()) {
			listAdapter.add(item);
		}
	}

	public void setPodcastList(PodcastItem[] values) {
		podcasts.clear();
		for (PodcastItem item : values) {
			podcasts.add(item);
		}
	}

	public List<PodcastItem> getPodcastList() {
		return podcasts;
	}
}
