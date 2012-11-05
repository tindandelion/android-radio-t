package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailRetriever {

    public interface Controller {
        boolean isInterrupted();
    }

    private PodcastList list;
    private ThumbnailProvider provider;
    private PodcastsConsumer consumer;

    public ThumbnailRetriever(PodcastList list, ThumbnailProvider provider, PodcastsConsumer consumer) {
        this.list = list;
        this.provider = provider;
        this.consumer = consumer;
    }

    public void retrieve(Controller controller) {
        for (PodcastItem item : list) {
            retrieveNext(item);
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
