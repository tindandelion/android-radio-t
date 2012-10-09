package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.podcasts.core.PodcastList;

public interface PodcastsProvider {
    PodcastList retrieve() throws Exception;
}
