package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public class CachingPodcastLoader {
    private final PodcastsProvider provider;
    private PodcastsCache cache;
    private PodcastsConsumer consumer;

    public CachingPodcastLoader(PodcastsProvider provider, PodcastsCache cache, PodcastsConsumer consumer) {
        this.provider = provider;
        this.cache = cache;
        this.consumer = consumer;
    }

    public void retrieve() throws Exception {
        if (cache.hasData()) {
            populateCacheData();
            refreshCacheIfExpired();
        } else {
            obtainNewData();
        }
    }

    private void refreshCacheIfExpired() throws Exception {
        if (cache.hasExpired()) {
            obtainNewData();
        }
    }

    private void populateCacheData() {
        PodcastList pl = cache.getData();
        consumer.updateList(pl);
    }

    private void obtainNewData() throws Exception {
        PodcastList pl = provider.retrieve();
        cache.updateWith(pl);
        consumer.updateList(pl);
    }
}
