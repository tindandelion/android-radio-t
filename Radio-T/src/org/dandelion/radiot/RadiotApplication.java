package org.dandelion.radiot;

import java.util.HashMap;

import org.dandelion.radiot.PodcastList.IPodcastListEngine;

import android.app.Application;

public class RadiotApplication extends Application {
	private HashMap<String, IPodcastListEngine> engines;

	@Override
	public void onCreate() {
		super.onCreate();
		engines = new HashMap<String, IPodcastListEngine>();
		engines.put("main-show", createPodcastEngine("http://feeds.rucast.net/radio-t"));
		engines.put("after-show", createPodcastEngine("http://feeds.feedburner.com/pirate-radio-t"));
	}

	protected IPodcastListEngine createPodcastEngine(String url) {
		return new PodcastListEngine(new RssFeedModel(url));
	}

	public IPodcastListEngine getPodcastEngine(String name) {
		return engines.get(name);
	}

	public void setPodcastEngine(String name, IPodcastListEngine engine) {
		engines.put(name, engine);
	}
}
