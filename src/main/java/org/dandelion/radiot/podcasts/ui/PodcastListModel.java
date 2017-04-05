package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public interface PodcastListModel {

    interface Factory {
        PodcastListModel create(String showName);
    }

    interface ProgressListener {
        void onStarted();

        void onFinished();

        void onError(String errorMessage);
    }

    interface Consumer {
        void updateList(PodcastList podcasts);
        void updateThumbnail(PodcastItem item, byte[] thumbnail);
    }

    void attach(ProgressListener progressListener, Consumer consumer);

    void release();

    void populateConsumer();

    void refreshData();
}
