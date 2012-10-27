package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailLoader {
    private PodcastList list;
    private ThumbnailProvider provider;
    private ThumbnailConsumer consumer;

    public ThumbnailLoader(PodcastList list, ThumbnailProvider provider, ThumbnailConsumer thumbnailConsumer) {
        this.list = list;
        this.provider = provider;
        this.consumer = thumbnailConsumer;
    }

    public void retrieve() {
        for(PodcastItem item : list) {
            byte[] thumbnail = provider.thumbnailDataFor(item.getThumbnailUrl());
            consumer.consume(item, thumbnail);
        }
    }
}
