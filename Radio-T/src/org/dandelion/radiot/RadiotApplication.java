package org.dandelion.radiot;

import android.app.Application;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;

import java.util.HashMap;

public class RadiotApplication extends Application {
    private HashMap<String, PodcastListLoader> engines;

    @Override
    public void onCreate() {
        super.onCreate();
        engines = new HashMap<String, PodcastListLoader>();
        engines.put("main-show",
                podcastEngine("http://feeds.rucast.net/radio-t", new HttpThumbnailDownloader()));
        engines.put(
                "after-show",
                podcastEngine("http://feeds.feedburner.com/pirate-radio-t", new NullThumbnailDownloader()));
        engines.put(
                "test-show",
                testPodcastEngine());
        PodcastsApp.initialize(this);
    }

    private PodcastListLoader testPodcastEngine() {
        return new AsyncPodcastListLoader(new RssFeedProvider("http://localhost:8080/rss", new NullThumbnailDownloader())) {
            @Override
            public void refresh(boolean resetCache) {
                super.refresh(true);
            }
        };
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    protected PodcastListLoader podcastEngine(String url, ThumbnailDownloader thumbnailDownloader) {
        return new AsyncPodcastListLoader(new RssFeedProvider(url, thumbnailDownloader));
    }

    public PodcastListLoader getPodcastEngine(String name) {
        return engines.get(name);
    }

    public void setPodcastEngine(String name, PodcastListLoader loader) {
        engines.put(name, loader);
    }
}
