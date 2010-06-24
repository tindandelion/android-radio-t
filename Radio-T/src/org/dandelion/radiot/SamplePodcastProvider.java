package org.dandelion.radiot;

import java.util.Date;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.PodcastListActivity.PodcastListAdapter;

public class SamplePodcastProvider implements IPodcastProvider {

	private PodcastItem[] podcasts;
	static final String LINK = "http://radio-t.com/downloads/rt_podcast190.mp3";
	private static final Date SAMPLE_DATE = new Date();

	public SamplePodcastProvider(PodcastItem[] podcasts) {
		this.podcasts = podcasts;
	}
	
	public SamplePodcastProvider() { 
		this(samplePodcastList());
	}

	public void retrievePodcasts(PodcastListAdapter listAdapter) {
		for (PodcastItem item : podcasts) {
			listAdapter.add(item);
		}
	}

	private static PodcastItem[] samplePodcastList() {
		return new PodcastItem[] {
				new PodcastItem(121, SAMPLE_DATE, "Show notes for 121",
						SamplePodcastProvider.LINK),
				new PodcastItem(122, SAMPLE_DATE, "Show notes for 122",
						SamplePodcastProvider.LINK),
				new PodcastItem(123, SAMPLE_DATE, "Show notes for 123",
						SamplePodcastProvider.LINK) };
	}

	public void setPodcastList(PodcastItem[] podcasts) {
		this.podcasts = podcasts;
	}
}
