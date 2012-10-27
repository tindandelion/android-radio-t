package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailRetriever {
    private PodcastList list;
    private ThumbnailProvider provider;
    private PodcastsConsumer consumer;

    public ThumbnailRetriever(PodcastList list, ThumbnailProvider provider, PodcastsConsumer consumer) {
        this.list = list;
        this.provider = provider;
        this.consumer = consumer;
    }

    public void retrieve() {
        for(PodcastItem item : list) {
            String url = item.getThumbnailUrl();
            if (url != null) {
                byte[] thumbnail = provider.thumbnailDataFor(url);
                consumer.updateThumbnail(item, thumbnail);
            }
        }
    }
}
