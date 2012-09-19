package org.dandelion.radiot.podcasts.core;

import java.util.List;

public interface PodcastListConsumer {
    void updatePodcasts(List<PodcastItem> podcasts);
    void updatePodcastImage(int index);
}
