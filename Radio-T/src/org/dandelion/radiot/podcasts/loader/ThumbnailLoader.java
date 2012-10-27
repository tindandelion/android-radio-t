package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailLoader {
    private PodcastList list;
    private ThumbnailProvider provider;
    private PodcastsConsumer consumer;

    public ThumbnailLoader(PodcastList list, ThumbnailProvider provider, PodcastsConsumer consumer) {
        this.list = list;
        this.provider = provider;
        this.consumer = consumer;
    }

    public void retrieve() {
        for(PodcastItem item : list) {
            byte[] thumbnail = provider.thumbnailDataFor(item.getThumbnailUrl());
            consumer.updateThumbnail(item, thumbnail);
        }
    }
}
