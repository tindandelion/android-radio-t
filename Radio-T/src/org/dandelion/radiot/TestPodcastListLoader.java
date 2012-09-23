package org.dandelion.radiot;

import android.content.Context;
import org.dandelion.radiot.podcasts.core.*;

import java.io.File;

public class TestPodcastListLoader extends AsyncPodcastListLoader {

    private static TestPodcastListLoader instance;

    public TestPodcastListLoader(PodcastsProvider podcasts, ThumbnailProvider thumbnails, PodcastsCache cache) {
        super(podcasts, thumbnails, cache);
    }

    public static PodcastListLoader create(Context context) {
        instance = new TestPodcastListLoader(
                new RssFeedProvider("http://localhost:8080/rss"),
                new NullThumbnailProvider(),
                createPodcastsCache(context));
        return instance;
    }

    private static PodcastsCache createPodcastsCache(Context context) {
        File cacheFile = new File(context.getCacheDir(), "test-cache");
        return new FilePodcastsCache(cacheFile);
    }

    public static void resetCache() {
        instance.cache.reset();
    }
}
