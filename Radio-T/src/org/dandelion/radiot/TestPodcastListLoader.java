package org.dandelion.radiot;

import android.content.Context;
import org.dandelion.radiot.podcasts.core.*;

import java.io.File;

public class TestPodcastListLoader extends AsyncPodcastListLoader {
    public static final int FORMAT_VERSION = 0;
    public static final String HOST = "http://localhost:8080";

    private static TestPodcastListLoader instance;

    public TestPodcastListLoader(PodcastsProvider podcasts, ThumbnailProvider thumbnails, PodcastsCache cache) {
        super(podcasts, thumbnails, cache);
    }

    public static PodcastListLoader create(Context context) {
        instance = new TestPodcastListLoader(
                new RssFeedProvider(HOST + "/rss"),
                new HttpThumbnailProvider(HOST),
                createPodcastsCache(context));
        return instance;
    }

    private static PodcastsCache createPodcastsCache(Context context) {
        File cacheFile = new File(context.getCacheDir(), "test-cache");
        return new FilePodcastsCache(cacheFile, FORMAT_VERSION);
    }

    public static void resetCache() {
        instance.cache.reset();
    }
}
