package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public class PodcastListRetriever {
    private final PodcastsProvider provider;
    private PodcastsCache cache;

    public PodcastListRetriever(PodcastsProvider provider, PodcastsCache cache) {
        this.provider = provider;
        this.cache = cache;
    }

    private boolean mustRefresh() {
        return !cache.hasValidData();
    }

    private void populateCachedDataTo(PodcastsConsumer consumer) {
        PodcastList pl = cache.getData();
        consumer.updateList(pl);
    }

    private void obtainNewDataTo(PodcastsConsumer consumer) throws Exception {
        PodcastList pl = provider.retrieve();
        cache.updateWith(pl);
        consumer.updateList(pl);
    }

    public void retrieveTo(PodcastsConsumer consumer, Boolean forceRefresh) throws Exception {
        if (!forceRefresh) {
            populateCachedDataTo(consumer);
        }

        if (forceRefresh || mustRefresh()) {
            obtainNewDataTo(consumer);
        }
    }
}
