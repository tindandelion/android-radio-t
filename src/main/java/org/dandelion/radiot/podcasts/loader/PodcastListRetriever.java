package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.ui.PodcastListModel;

class PodcastListRetriever {
    private final PodcastsProvider provider;
    private PodcastsCache cache;

    PodcastListRetriever(PodcastsProvider provider, PodcastsCache cache) {
        this.provider = provider;
        this.cache = cache;
    }

    private boolean mustRefresh() {
        return !cache.hasValidData();
    }

    private void populateCachedDataTo(PodcastListModel.Consumer consumer) {
        PodcastList pl = cache.getData();
        consumer.updateList(pl);
    }

    private void obtainNewDataTo(PodcastListModel.Consumer consumer) throws Exception {
        PodcastList pl = provider.retrieve();
        cache.updateWith(pl);
        consumer.updateList(pl);
    }

    void retrieveTo(PodcastListModel.Consumer consumer, Boolean forceRefresh) throws Exception {
        if (!forceRefresh) {
            populateCachedDataTo(consumer);
        }

        if (forceRefresh || mustRefresh()) {
            obtainNewDataTo(consumer);
        }
    }
}
