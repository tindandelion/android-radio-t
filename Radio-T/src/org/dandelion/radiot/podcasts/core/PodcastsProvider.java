package org.dandelion.radiot.podcasts.core;

public interface PodcastsProvider {
    PodcastList retrieve() throws Exception;
}
