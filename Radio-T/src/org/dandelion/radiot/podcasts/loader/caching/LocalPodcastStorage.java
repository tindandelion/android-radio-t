package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.podcasts.loader.PodcastsCache;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;

import java.io.File;

public class LocalPodcastStorage {
    private static final int CACHE_FORMAT_VERSION = 1;
    public static final int THUMBNAIL_CACHE_LIMIT = 30;

    private FilePodcastsCache podcastsCache;
    private FileThumbnailCache thumbnailsCache;

    public LocalPodcastStorage(String showName, File baseCacheDir) {
        File cacheDir = new File(baseCacheDir, showName);
        File podcastsFile = new File(cacheDir, "podcasts.dat");
        File thumbnailsDir = new File(cacheDir, "thumbnails");

        ensureDirExists(cacheDir);
        ensureDirExists(thumbnailsDir);

        podcastsCache = new FilePodcastsCache(podcastsFile, CACHE_FORMAT_VERSION);
        thumbnailsCache = new FileThumbnailCache(thumbnailsDir, THUMBNAIL_CACHE_LIMIT);
    }

    public PodcastsCache newPodcastsCache() {
        return podcastsCache;
    }

    public ThumbnailCache newThumbnailCache() {
        return thumbnailsCache;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void ensureDirExists(File dir) {
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
