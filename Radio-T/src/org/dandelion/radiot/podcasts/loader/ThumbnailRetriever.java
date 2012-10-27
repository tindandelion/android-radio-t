package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

import java.util.Iterator;

public class ThumbnailRetriever {
    private ThumbnailProvider provider;
    private PodcastsConsumer consumer;
    private Iterator<PodcastItem> iterator;

    public ThumbnailRetriever(PodcastList list, ThumbnailProvider provider, PodcastsConsumer consumer) {
        this.iterator = list.iterator();
        this.provider = provider;
        this.consumer = consumer;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public void retrieveNext() {
        PodcastItem item = iterator.next();
        String url = item.getThumbnailUrl();
        if (url != null) {
            byte[] thumbnail = provider.thumbnailDataFor(url);
            consumer.updateThumbnail(item, thumbnail);
        }
    }
}
