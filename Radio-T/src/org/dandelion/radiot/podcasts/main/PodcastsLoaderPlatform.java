package org.dandelion.radiot.podcasts.main;

import android.content.Context;
import org.dandelion.radiot.podcasts.loader.*;

import java.io.File;

public class PodcastsLoaderPlatform implements LoaderFactory {
    private static final int CACHE_FORMAT_VERSION = 5;
    private static final String THUMBNAIL_HOST = "http://www.radio-t.com";

    private Context context;

    public PodcastsLoaderPlatform(Context context) {
        this.context = context;
    }

    @Override
    public PodcastListLoader createLoaderForShow(String name) {
        PodcastProperties props = PodcastProperties.propertiesForShow(name);
        return createPodcastLoader(props);
    }

    private PodcastListLoader createPodcastLoader(PodcastProperties props) {
        HttpThumbnailProvider thumbnails = new HttpThumbnailProvider(THUMBNAIL_HOST);
        return new AsyncPodcastListLoader(
                new RssFeedProvider(props.url, thumbnails),
                createPodcastsCache(props.name));
    }

    private PodcastsCache createPodcastsCache(String name) {
        File cacheFile = new File(context.getCacheDir(), name);
        return new FilePodcastsCache(cacheFile, CACHE_FORMAT_VERSION);
    }
}
