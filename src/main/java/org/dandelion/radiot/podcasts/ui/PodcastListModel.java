package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.podcasts.loader.PodcastsConsumer;
import org.dandelion.radiot.podcasts.loader.ProgressListener;

public interface PodcastListModel {

    interface Factory {
        PodcastListModel create(String showName);
    }

    void attach(ProgressListener progressListener, PodcastsConsumer consumer);

    void release();

    void populateConsumer();

    void refreshData();
}
