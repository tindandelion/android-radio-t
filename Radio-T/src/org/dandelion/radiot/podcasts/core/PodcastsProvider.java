package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

import java.util.List;

public interface PodcastsProvider {
    List<PodcastItem> retrieveAll() throws Exception;
    Bitmap thumbnailFor(PodcastItem item);
}
