package org.dandelion.radiot.podcasts.core;

import java.util.List;

class MemoryCache implements PodcastsCache {
    public List<PodcastItem> data;

    @Override
    public void reset() {
        data = null;
    }

    @Override
    public List<PodcastItem> getData() {
        return data;
    }

    @Override
    public void updateWith(List<PodcastItem> data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return data != null;
    }
}
