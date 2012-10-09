package org.dandelion.radiot.podcasts.loader;

public interface LoaderFactory {
    PodcastListLoader createLoaderForShow(String name);
}
