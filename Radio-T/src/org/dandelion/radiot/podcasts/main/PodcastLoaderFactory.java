package org.dandelion.radiot.podcasts.main;

import org.dandelion.radiot.podcasts.loader.LoaderFactory;
import org.dandelion.radiot.podcasts.loader.PodcastListLoader;

public class PodcastLoaderFactory implements LoaderFactory {
    private PodcastsApp app;

    public PodcastLoaderFactory(PodcastsApp podcastsApp) {
        this.app = podcastsApp;
    }

    @Override
    public PodcastListLoader createLoaderForShow(String name) {
        PodcastProperties props = PodcastProperties.propertiesForShow(name);
        return app.createPodcastLoader(props);
    }
}
