package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public interface PodcastsCache {
    void reset();

    PodcastList getData();

    void updateWith(PodcastList data);

    boolean hasData();

    boolean hasExpired();
}
