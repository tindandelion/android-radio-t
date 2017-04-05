package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.ui.PodcastListModel;

class ThumbnailRetriever {

    public interface Controller {
        boolean isInterrupted();
    }

    private final HttpClient httpClient;
    private ThumbnailCache cache;

    ThumbnailRetriever(HttpClient httpClient, ThumbnailCache cache) {
        this.httpClient = httpClient;
        this.cache = cache;
    }

    void retrieve(PodcastList pl, PodcastListModel.Consumer consumer, Controller controller) {
        PodcastList toDownload = retrieveCached(pl, consumer);
        downloadRemote(toDownload, controller, consumer);
    }

    private void downloadRemote(PodcastList items, Controller controller, PodcastListModel.Consumer consumer) {
        for (PodcastItem pi : items) {
            if (controller.isInterrupted()) {
                break;
            }
            downloadForItem(pi, consumer);
        }
    }

    private void downloadForItem(PodcastItem pi, PodcastListModel.Consumer consumer) {
        try {
            byte[] result = httpClient.getByteContent(pi.thumbnailUrl);
            cache.update(pi.thumbnailUrl, result);
            consumer.updateThumbnail(pi, result);
        } catch (Exception ignored) {
        }
    }

    private PodcastList retrieveCached(PodcastList pl, PodcastListModel.Consumer consumer) {
        PodcastList cacheMisses = new PodcastList();
        for (PodcastItem pi : pl) {
            if (pi.hasThumbnail()) {
                tryToFetchFromCache(pi, cacheMisses, consumer);
            }
        }
        return cacheMisses;
    }

    private void tryToFetchFromCache(PodcastItem pi, PodcastList cacheMisses, PodcastListModel.Consumer consumer) {
        byte[] thumbnail = cache.lookup(pi.thumbnailUrl);
        if (thumbnail == null) {
            cacheMisses.add(pi);
        } else {
            consumer.updateThumbnail(pi, thumbnail);
        }
    }

}
