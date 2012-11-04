package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.PodcastsCache;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;

import java.io.File;

public class LocalPodcastStorage implements FilePodcastsCache.Listener {
    private static final int CACHE_FORMAT_VERSION = 1;

    private FilePodcastsCache podcastsCache;
    private FileThumbnailCache thumbnailsCache;

    public LocalPodcastStorage(String showName, File baseCacheDir) {
        File cacheDir = new File(baseCacheDir, showName);
        File podcastsFile = new File(cacheDir, "podcasts.dat");
        File thumbnailsDir = new File(cacheDir, "thumbnails");

        ensureDirExists(cacheDir);
        ensureDirExists(thumbnailsDir);

        thumbnailsCache = new FileThumbnailCache(thumbnailsDir);
        podcastsCache = new FilePodcastsCache(podcastsFile, CACHE_FORMAT_VERSION);
        podcastsCache.setListener(this);
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

    @Override
    public void onUpdatedWith(PodcastList list) {
        thumbnailsCache.cleanup(list.collectThumbnails());
    }
}
