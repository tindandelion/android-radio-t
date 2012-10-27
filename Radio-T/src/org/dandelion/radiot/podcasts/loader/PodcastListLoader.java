package org.dandelion.radiot.podcasts.loader;

public interface PodcastListLoader {
    void refreshFromServer();
    void refreshFromCache();
    void detach();
    void attach(ProgressListener listener, PodcastsConsumer consumer);
}
