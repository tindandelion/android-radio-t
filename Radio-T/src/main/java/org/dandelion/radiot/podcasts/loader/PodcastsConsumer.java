package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public interface PodcastsConsumer {
    void updateList(PodcastList podcasts);
    void updateThumbnail(PodcastItem item, byte[] thumbnail);

    public static PodcastsConsumer Null = new PodcastsConsumer() {
        @Override
        public void updateList(PodcastList podcasts) {
        }

        @Override
        public void updateThumbnail(PodcastItem item, byte[] thumbnail) {
        }
    };
}
