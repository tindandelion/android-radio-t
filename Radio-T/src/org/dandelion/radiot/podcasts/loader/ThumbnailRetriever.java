package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailRetriever {

    public interface Controller {
        boolean isInterrupted();
    }

    private ThumbnailProvider provider;
    private PodcastsConsumer consumer;

    public ThumbnailRetriever(ThumbnailProvider provider, PodcastsConsumer consumer) {
        this.provider = provider;
        this.consumer = consumer;
    }

    public void retrieve(PodcastList pl, Controller controller) {
        for (PodcastItem pi : pl) {
            retrieveNext(pi);
            if (controller.isInterrupted()) {
                break;
            }
        }
    }

    private void retrieveNext(PodcastItem pi) {
        final String url = pi.thumbnailUrl;
        if (url != null) {
            byte[] thumbnail = provider.thumbnailDataFor(url);
            consumer.updateThumbnail(pi, thumbnail);
        }
    }
}
