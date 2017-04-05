package org.dandelion.radiot.podcasts.main;

import android.content.Context;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.OkBasedHttpClient;
import org.dandelion.radiot.podcasts.loader.*;
import org.dandelion.radiot.podcasts.loader.caching.LocalPodcastStorage;
import org.dandelion.radiot.podcasts.ui.PodcastListModel;

import java.util.HashMap;

public class PodcastClientPlatform implements PodcastListModel.Factory {

    private static HashMap<String, PodcastProperties> shows;

    static {
        shows = new HashMap<>();
        shows.put("main-show",
                new PodcastProperties("main-show",
                        "http://feeds.rucast.net/radio-t"));
        shows.put("after-show",
                new PodcastProperties("after-show",
                        "http://feeds.feedburner.com/pirate-radio-t"));
    }

    private Context context;

    protected PodcastClientPlatform(Context context) {
        this.context = context;
    }

    @Override
    public PodcastListModel create(String showName) {
        PodcastProperties props = propertiesForShow(showName);
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
                newThumbnailClient(), newThumbnailCache(localStorage)
        );
    }

    private PodcastsProvider newFeedProvider(String address) {
        return new RssFeedProvider(address);
    }

    protected HttpClient newThumbnailClient() {
        return OkBasedHttpClient.make();
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

        PodcastProperties(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }

}
