package org.dandelion.radiot.podcasts.core;

public interface PodcastsCache {
    void reset();

    PodcastList getData();

    void updateWith(PodcastList data);

    boolean hasData();

    boolean hasExpired();
}
