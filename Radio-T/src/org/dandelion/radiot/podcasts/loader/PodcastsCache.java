package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public interface PodcastsCache {
    boolean hasValidData();
    PodcastList getData();
    void updateWith(PodcastList data);
}
