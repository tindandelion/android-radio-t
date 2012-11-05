package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailRetriever {


    public interface Controller {
        boolean isInterrupted();
    }

    private ThumbnailCache cache;
    private ThumbnailProvider provider;
    private PodcastsConsumer consumer;

    public ThumbnailRetriever(ThumbnailProvider provider, ThumbnailCache cache, PodcastsConsumer consumer) {
        this.provider = provider;
        this.cache = cache;
        this.consumer = consumer;
    }

    public void retrieve(PodcastList pl, Controller controller) {
        for (PodcastItem pi : pl) {
            retrieveThumbnailFor(pi);
            if (controller.isInterrupted()) {
                break;
            }
        }
    }

    private void retrieveThumbnailFor(PodcastItem pi) {
        final String url = pi.thumbnailUrl;
        if (url != null) {
            byte[] thumbnail = retrieveByUrl(url);
            consumer.updateThumbnail(pi, thumbnail);
        }
    }

    private byte[] retrieveByUrl(String url) {
        byte[] value = cache.lookup(url);
        if (value == null) {
            value = provider.thumbnailDataFor(url);
            cache.update(url, value);
        }
        return value;
    }
}
