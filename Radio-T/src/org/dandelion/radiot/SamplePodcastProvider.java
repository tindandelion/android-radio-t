package org.dandelion.radiot;

import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;

// TODO Get rid of SamplePodcastProvider
public class SamplePodcastProvider implements IPodcastProvider {

	private List<PodcastItem> podcasts = new ArrayList<PodcastItem>();

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
