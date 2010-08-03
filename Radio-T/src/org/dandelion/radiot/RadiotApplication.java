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

	private HashMap<String, IPodcastListEngine> engines;
	
	@Override
	public void onCreate() {
		super.onCreate();
		engines = new HashMap<String, IPodcastListEngine>();
		engines.put("main-show", PodcastList.getEngine("http://feeds.rucast.net/radio-t"));
		engines.put("after-show", PodcastList.getEngine("http://feeds.feedburner.com/pirate-radio-t"));
	}

	public IPodcastListEngine getPodcastEngine(String name) {
		return engines.get(name);
	}
	
	public void setPodcastEngine(String name, IPodcastListEngine engine) {
		engines.put(name, engine);
	}
}
