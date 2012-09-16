package org.dandelion.radiot;

import android.app.Application;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;

import java.util.HashMap;

public class RadiotApplication extends Application {
    private HashMap<String, IPodcastListEngine> engines;

    @Override
    public void onCreate() {
        super.onCreate();
        engines = new HashMap<String, IPodcastListEngine>();
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

    private IPodcastListEngine testPodcastEngine() {
        return new PodcastListEngine(new RssFeedModel("http://localhost:8080/rss", new NullThumbnailDownloader())) {
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

    protected IPodcastListEngine podcastEngine(String url, PodcastList.ThumbnailDownloader thumbnailDownloader) {
        return new PodcastListEngine(new RssFeedModel(url, thumbnailDownloader));
    }

    public IPodcastListEngine getPodcastEngine(String name) {
        return engines.get(name);
    }

    public void setPodcastEngine(String name, IPodcastListEngine engine) {
        engines.put(name, engine);
    }
}
