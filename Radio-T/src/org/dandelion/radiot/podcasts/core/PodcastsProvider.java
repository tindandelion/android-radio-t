package org.dandelion.radiot.podcasts.core;

import java.util.List;

public interface PodcastsProvider {
    List<PodcastItem> retrieveAll() throws Exception;
}
