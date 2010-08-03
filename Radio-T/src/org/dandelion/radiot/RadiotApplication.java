package org.dandelion.radiot;

import java.util.HashMap;

import org.dandelion.radiot.PodcastList.IPodcastListEngine;

import android.app.Application;

public class RadiotApplication extends Application {
	private static HashMap<String, String> podcastUrls = new HashMap<String, String>();
	static {
		podcastUrls.put("main-show", "http://feeds.rucast.net/radio-t");
		podcastUrls.put("after-show", "http://feeds.feedburner.com/pirate-radio-t");
	}

	public IPodcastListEngine getPodcastEngine(String name) {
		return PodcastList.getPresenter(podcastUrls.get(name));
	}
}
