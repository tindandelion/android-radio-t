package org.dandelion.radiot;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.PodcastListActivity.PodcastListAdapter;

public class SamplePodcastProvider implements IPodcastProvider {

	private PodcastInfo[] podcasts;
	static final String LINK = "http://radio-t.com/downloads/rt_podcast190.mp3";

	public SamplePodcastProvider(PodcastInfo[] podcasts) {
		this.podcasts = podcasts;
	}
	
	public SamplePodcastProvider() { 
		this(samplePodcastList());
	}

	public void retrievePodcasts(PodcastListAdapter listAdapter) {
		for (PodcastInfo item : podcasts) {
			listAdapter.add(item);
		}
	}

	private static PodcastInfo[] samplePodcastList() {
		return new PodcastInfo[] {
				new PodcastInfo("#121", "18.06.2010", "Show notes for 121",
						SamplePodcastProvider.LINK),
				new PodcastInfo("#122", "19.06.2010", "Show notes for 122",
						SamplePodcastProvider.LINK),
				new PodcastInfo("#123", "20.06.2010", "Show notes for 123",
						SamplePodcastProvider.LINK) };
	}

	public void setPodcastList(PodcastInfo[] podcasts) {
		this.podcasts = podcasts;
	}
}
