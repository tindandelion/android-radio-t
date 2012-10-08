package org.dandelion.radiot.podcasts.core;

public class CachingPodcastLoader {
    private final PodcastsProvider podcasts;
    private PodcastsCache cache;

    public CachingPodcastLoader(PodcastsProvider podcasts, PodcastsCache cache) {
        this.podcasts = podcasts;
        this.cache = cache;
    }

    public void retrieveTo(PodcastsConsumer consumer) throws Exception {
        if (cache.hasData()) {
            populateCachedData(consumer);
            refreshCachedDataIfExpired(consumer);
        } else {
            obtainNewData(consumer);
        }
    }

    private void refreshCachedDataIfExpired(PodcastsConsumer consumer) throws Exception {
        if (cache.hasExpired()) {
            obtainNewData(consumer);
        }
    }

    private void populateCachedData(PodcastsConsumer consumer) {
        PodcastList pl = cache.getData();
        consumer.updatePodcasts(pl);
    }

    private void obtainNewData(PodcastsConsumer consumer) throws Exception {
        PodcastList pl = podcasts.retrieve();
        cache.updateWith(pl);
        consumer.updatePodcasts(pl);
    }
}
