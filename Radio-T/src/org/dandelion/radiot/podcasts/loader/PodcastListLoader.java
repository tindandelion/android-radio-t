package org.dandelion.radiot.podcasts.loader;

public interface PodcastListLoader {
    void refresh(boolean resetCache);
    void cancelUpdate();
    void detach();
    void attach(ProgressListener listener, PodcastsConsumer consumer);
}
