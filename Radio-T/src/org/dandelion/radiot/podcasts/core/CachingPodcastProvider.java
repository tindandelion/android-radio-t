package org.dandelion.radiot.podcasts.core;

import java.util.List;

public class CachingPodcastProvider implements PodcastsProvider {
    private final PodcastsProvider podcasts;
    private PodcastsCache cache;

    public CachingPodcastProvider(PodcastsProvider podcasts, PodcastsCache cache) {
        this.podcasts = podcasts;
        this.cache = cache;
    }

    @Override
    public List<PodcastItem> retrieveAll() throws Exception {
        if (cache.isValid()) {
            return cache.getData();
        } else {
            return updateCacheWith(podcasts.retrieveAll());
        }
    }

    private List<PodcastItem> updateCacheWith(List<PodcastItem> newData) {
        cache.updateWith(newData);
        return newData;
    }

    public void reset() {
        cache.reset();
    }

}
