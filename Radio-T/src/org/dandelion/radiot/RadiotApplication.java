package org.dandelion.radiot;

import android.app.Application;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;

import java.util.HashMap;

public class RadiotApplication extends Application {
    private static final String THUMBNAIL_HOST = "http://www.radio-t.com";
    private HashMap<String, PodcastListLoader> engines;

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        createEngines();
    }

    protected void createEngines() {
        engines = new HashMap<String, PodcastListLoader>();
        engines.put("main-show",
                podcastLoader("main-show", "http://feeds.rucast.net/radio-t", THUMBNAIL_HOST));
        engines.put("after-show",
                podcastLoader("after-show", "http://feeds.feedburner.com/pirate-radio-t", THUMBNAIL_HOST));
        engines.put(
                "test-show",
                podcastLoader("test-show", TestPodcastListLoader.RSS_URL, TestPodcastListLoader.BASE_URL));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    protected PodcastListLoader podcastLoader(String showName, String url, String thumbnailHost) {
        return PodcastsApp.getInstance().createPodcastLoader(showName, url, thumbnailHost);
    }

    public PodcastListLoader getPodcastEngine(String name) {
        return engines.get(name);
    }

    public void setPodcastEngine(String name, PodcastListLoader loader) {
        engines.put(name, loader);
    }

}
