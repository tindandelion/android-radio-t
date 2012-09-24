package org.dandelion.radiot.podcasts.core;

public interface PodcastsProvider {
    PodcastList retrieveAll() throws Exception;
}
