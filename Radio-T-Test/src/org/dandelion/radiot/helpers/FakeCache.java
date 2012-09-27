package org.dandelion.radiot.helpers;

import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.core.PodcastsCache;

public class FakeCache implements PodcastsCache {
    @Override
    public void reset() {
    }

    @Override
    public PodcastList getData() {
        throw new RuntimeException("Should not be called");
    }

    @Override
    public void updateWith(PodcastList data) {
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
