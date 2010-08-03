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
	}

	public IPodcastListEngine getPodcastEngine(String name) {
		IPodcastListEngine engine = engines.get(name);
		if (null == engine) { 
			engine = PodcastList.getEngine(podcastUrls.get(name));
			engines.put(name, engine);
		}
		return engine;
	}

	public void resetEngines() {
		engines.clear();
	}
}
