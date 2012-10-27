package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public class CachingPodcastLoader {
    private final PodcastsProvider podcasts;
    private PodcastsCache cache;

    public CachingPodcastLoader(PodcastsProvider podcasts, PodcastsCache cache) {
        this.podcasts = podcasts;
        this.cache = cache;
    }

    public void resetCache() {
        cache.reset();
    }

    public void retrieveTo(PodcastsConsumer consumer) throws Exception {
        if (cache.hasData()) {
            populateCacheData(consumer);
            refreshCacheIfExpired(consumer);
        } else {
            obtainNewData(consumer);
        }
    }

    private void refreshCacheIfExpired(PodcastsConsumer consumer) throws Exception {
        if (cache.hasExpired()) {
            obtainNewData(consumer);
        }
    }

    private void populateCacheData(PodcastsConsumer consumer) {
        PodcastList pl = cache.getData();
        consumer.updatePodcasts(pl);
    }

    private void obtainNewData(PodcastsConsumer consumer) throws Exception {
        PodcastList pl = podcasts.retrieve();
        cache.updateWith(pl);
        consumer.updatePodcasts(pl);
    }
}
