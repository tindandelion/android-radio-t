package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.podcasts.loader.PodcastListClient;

public interface PodcastClientFactory {
    PodcastListClient createLoaderForShow(String name);
}
