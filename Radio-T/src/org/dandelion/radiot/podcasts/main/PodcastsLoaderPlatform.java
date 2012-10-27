package org.dandelion.radiot.podcasts.main;

import android.content.Context;
import org.dandelion.radiot.podcasts.loader.*;
import org.dandelion.radiot.podcasts.ui.PodcastLoaderFactory;

import java.io.File;
import java.util.HashMap;

public class PodcastsLoaderPlatform implements PodcastLoaderFactory {
    private static final int CACHE_FORMAT_VERSION = 5;

    private static HashMap<String, PodcastProperties> shows;
    static {
        shows = new HashMap<String, PodcastProperties>();
        shows.put("main-show",
                new PodcastProperties("main-show",
                        "http://feeds.rucast.net/radio-t"));
        shows.put("after-show",
                new PodcastProperties("after-show",
                        "http://feeds.feedburner.com/pirate-radio-t"));
    }

    private Context context;

    public PodcastsLoaderPlatform(Context context) {
        this.context = context;
    }

    @Override
    public PodcastListLoader createLoaderForShow(String name) {
        PodcastProperties props = propertiesForShow(name);
        return createPodcastLoader(props);
    }

    private PodcastProperties propertiesForShow(String name) {
        return shows.get(name);
    }

    private PodcastListLoader createPodcastLoader(PodcastProperties props) {
        return new AsyncPodcastListLoader(
                new RssFeedProvider(props.url),
                createPodcastsCache(props.name));
    }

    private PodcastsCache createPodcastsCache(String name) {
        File cacheFile = new File(context.getCacheDir(), name);
        return new FilePodcastsCache(cacheFile, CACHE_FORMAT_VERSION);
    }

    private static class PodcastProperties {
        public String name;
        public String url;

        public PodcastProperties(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }
}
