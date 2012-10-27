package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public interface PodcastsConsumer {
    void updateList(PodcastList podcasts);

    public static PodcastsConsumer Null = new PodcastsConsumer() {
        @Override
        public void updateList(PodcastList podcasts) {
        }
    };
}
