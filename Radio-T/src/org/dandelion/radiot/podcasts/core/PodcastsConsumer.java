package org.dandelion.radiot.podcasts.core;

public interface PodcastsConsumer {
    void updatePodcasts(PodcastList podcasts);

    public static PodcastsConsumer Null = new PodcastsConsumer() {
        @Override
        public void updatePodcasts(PodcastList podcasts) {
        }
    };
}
