package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;

import java.io.File;

public class LocalPodcastStorage {
    private static final int CACHE_FORMAT_VERSION = 1;

    private FilePodcastsCache podcastsCache;
    private FileThumbnailCache thumbnailsCache;

    public LocalPodcastStorage(String showName, File baseCacheDir) {
        File cacheDir = new File(baseCacheDir, showName);
        File podcastsFile = new File(cacheDir, "podcasts.dat");
        File thumbnailsDir = new File(cacheDir, "thumbnails");

        ensureDirExists(cacheDir);
        ensureDirExists(thumbnailsDir);

        podcastsCache = new FilePodcastsCache(podcastsFile, CACHE_FORMAT_VERSION);
        thumbnailsCache = new FileThumbnailCache(thumbnailsDir);
    }

    public PodcastsCache podcastsCache() {
        return podcastsCache;
    }

    public ThumbnailCache thumbnailsCache() {
        return thumbnailsCache;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void ensureDirExists(File dir) {
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void cleanupThumbnails(PodcastList currentItems) {
        thumbnailsCache.cleanup(currentItems.collectThumbnails());
    }
}
