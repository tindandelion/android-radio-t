package org.dandelion.radiot.podcasts.core;

public class CachingPodcastLoader {
    private final PodcastsProvider podcasts;
    private PodcastsCache cache;

    public CachingPodcastLoader(PodcastsProvider podcasts, PodcastsCache cache) {
        this.podcasts = podcasts;
        this.cache = cache;
    }

    public PodcastList retrieve() throws Exception {
        if (cache.isValid()) {
            return cache.getData();
        } else {
            return updateCacheWith(podcasts.retrieve());
        }
    }

    private PodcastList updateCacheWith(PodcastList newData) {
        cache.updateWith(newData);
        return newData;
    }

    public void retrieveTo(PodcastsConsumer consumer) throws Exception {
        PodcastList pl = retrieve();
        consumer.updatePodcasts(pl);
    }
}
