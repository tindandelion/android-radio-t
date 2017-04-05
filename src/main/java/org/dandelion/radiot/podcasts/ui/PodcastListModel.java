package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.podcasts.loader.PodcastsConsumer;

public interface PodcastListModel {

    interface Factory {
        PodcastListModel create(String showName);
    }

    interface ProgressListener {
        void onStarted();

        void onFinished();

        void onError(String errorMessage);
    }

    void attach(ProgressListener progressListener, PodcastsConsumer consumer);

    void release();

    void populateConsumer();

    void refreshData();
}
