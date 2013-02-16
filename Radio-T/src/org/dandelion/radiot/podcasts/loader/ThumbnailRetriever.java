package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class ThumbnailRetriever {


    public interface Controller {
        boolean isInterrupted();
    }

    private ThumbnailCache cache;
    private ThumbnailProvider provider;

    public ThumbnailRetriever(ThumbnailProvider provider, ThumbnailCache cache) {
        this.provider = provider;
        this.cache = cache;
    }

    public void retrieve(PodcastList pl, PodcastsConsumer consumer, Controller controller) {
        PodcastList toDownload = retrieveCached(pl, consumer);
        downloadRemote(toDownload, controller, consumer);
    }

    private void downloadRemote(PodcastList items, Controller controller, PodcastsConsumer consumer) {
        for (PodcastItem pi : items) {
            byte[] thumbnail = downloadByUrl(pi.thumbnailUrl);
            consumer.updateThumbnail(pi, thumbnail);
            if (controller.isInterrupted()) {
                break;
            }
        }
    }

    private PodcastList retrieveCached(PodcastList pl, PodcastsConsumer consumer) {
        PodcastList cacheMisses = new PodcastList();
        for (PodcastItem pi : pl) {
            if (pi.hasThumbnail()) {
                tryToFetchFromCache(pi, cacheMisses, consumer);
            }
        }
        return cacheMisses;
    }

    private void tryToFetchFromCache(PodcastItem pi, PodcastList cacheMisses, PodcastsConsumer consumer) {
        byte[] thumbnail = cache.lookup(pi.thumbnailUrl);
        if (thumbnail == null) {
            cacheMisses.add(pi);
        } else {
            consumer.updateThumbnail(pi, thumbnail);
        }
    }

    private byte[] downloadByUrl(String url) {
        byte[] value = provider.thumbnailDataFor(url);
        cache.update(url, value);
        return value;
    }
}
