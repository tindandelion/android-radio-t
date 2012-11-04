package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public class PodcastListRetriever {
    private final PodcastsProvider provider;
    private PodcastsCache cache;
    private PodcastsConsumer consumer;

    public PodcastListRetriever(PodcastsProvider provider, PodcastsCache cache, PodcastsConsumer consumer) {
        this.provider = provider;
        this.cache = cache;
        this.consumer = consumer;
    }

    public void retrieve() throws Exception {
        populateCachedData();
        if (shouldRefresh()) {
            obtainNewData();
        }
    }

    private boolean shouldRefresh() {
        return !cache.hasValidData();
    }

    private void populateCachedData() {
        PodcastList pl = cache.getData();
        consumer.updateList(pl);
    }

    private void obtainNewData() throws Exception {
        PodcastList pl = provider.retrieve();
        cache.updateWith(pl);
        consumer.updateList(pl);
    }
}
