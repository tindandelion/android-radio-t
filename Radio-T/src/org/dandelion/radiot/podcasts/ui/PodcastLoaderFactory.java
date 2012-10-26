package org.dandelion.radiot.podcasts.ui;

import org.dandelion.radiot.podcasts.loader.PodcastListLoader;

public interface PodcastLoaderFactory {
    PodcastListLoader createLoaderForShow(String name);
}
