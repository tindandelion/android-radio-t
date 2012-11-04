package org.dandelion.radiot.podcasts.loader;

import java.io.File;

public class LocalPodcastStorage {
    private static final int CACHE_FORMAT_VERSION = 1;
    public static final int THUMBNAIL_CACHE_LIMIT = 30;

    private File cacheDir;

    public LocalPodcastStorage(String showName, File baseCacheDir) {
        this.cacheDir = new File(baseCacheDir, showName);
    }

    public PodcastsCache newPodcastsCache() {
        makeCacheDir();
        File cacheFile = new File(cacheDir, "podcasts.dat");
        return new FilePodcastsCache(cacheFile, CACHE_FORMAT_VERSION);
    }


    public ThumbnailCache newThumbnailCache() {
        makeCacheDir();
        File thumbnails = new File(cacheDir, "thumbnails");
        return new FileThumbnailCache(thumbnails, THUMBNAIL_CACHE_LIMIT);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void makeCacheDir() {
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }
}
