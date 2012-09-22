package org.dandelion.radiot.podcasts.core;

import java.util.List;

public class MemoryPodcastCache implements PodcastsProvider {
    private final PodcastsProvider podcasts;
    private List<PodcastItem> cachedResult;

    public MemoryPodcastCache(PodcastsProvider podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public List<PodcastItem> retrieveAll() throws Exception {
        if (cachedResult == null) {
            cachedResult = podcasts.retrieveAll();
        }
        return cachedResult;
    }
}
