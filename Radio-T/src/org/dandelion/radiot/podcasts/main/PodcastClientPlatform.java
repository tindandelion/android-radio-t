package org.dandelion.radiot.podcasts.main;

import android.content.Context;
import org.dandelion.radiot.podcasts.loader.*;
import org.dandelion.radiot.podcasts.loader.caching.LocalPodcastStorage;
import org.dandelion.radiot.podcasts.ui.PodcastClientFactory;

import java.util.HashMap;

public class PodcastClientPlatform implements PodcastClientFactory {
    private static final String THUMBNAIL_HOST = "http://www.radio-t.com";

    private static HashMap<String, PodcastProperties> shows;

    static {
        shows = new HashMap<String, PodcastProperties>();
        shows.put("main-show",
                new PodcastProperties("main-show",
//                        "http://192.168.5.108:4567/rss"));
                        "http://feeds.rucast.net/radio-t"));
        shows.put("after-show",
                new PodcastProperties("after-show",
                        "http://feeds.feedburner.com/pirate-radio-t"));
    }

    private Context context;

    public PodcastClientPlatform(Context context) {
        this.context = context;
    }

    @Override
    public PodcastListClient newClientForShow(String name) {
        PodcastProperties props = propertiesForShow(name);
        return newLoaderWithProperties(props);
    }

    private PodcastProperties propertiesForShow(String name) {
        return shows.get(name);
    }

    private PodcastListClient newLoaderWithProperties(PodcastProperties props) {
        LocalPodcastStorage localStorage = new LocalPodcastStorage(props.name,
                context.getCacheDir());
        return new PodcastListClient(
                newFeedProvider(props.url),
                newPodcastsCache(localStorage),
                newThumbnailProvider(THUMBNAIL_HOST, localStorage),
                newThumbnailCache(localStorage));
    }

    private PodcastsProvider newFeedProvider(String address) {
        return new RssFeedProvider(address);
    }

    protected ThumbnailProvider newThumbnailProvider(String address, LocalPodcastStorage localStorage) {
        return new HttpThumbnailProvider(address);
    }

    private ThumbnailCache newThumbnailCache(LocalPodcastStorage localStorage) {
        return localStorage.thumbnailsCache();
    }

    private PodcastsCache newPodcastsCache(LocalPodcastStorage localStorage) {
        return localStorage.podcastsCache();
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
