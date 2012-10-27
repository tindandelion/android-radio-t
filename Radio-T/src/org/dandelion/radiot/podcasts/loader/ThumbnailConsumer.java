package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;

public interface ThumbnailConsumer {
    void consume(PodcastItem item, byte[] thumbnail);
}
