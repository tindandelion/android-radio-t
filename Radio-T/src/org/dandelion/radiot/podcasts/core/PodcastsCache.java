package org.dandelion.radiot.podcasts.core;

import java.util.List;

public interface PodcastsCache {
    void reset();

    List<PodcastItem> getData();

    void updateWith(List<PodcastItem> data);

    boolean isValid();
}
