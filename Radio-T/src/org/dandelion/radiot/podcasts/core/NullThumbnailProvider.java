package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public class NullThumbnailProvider implements ThumbnailProvider {

    @Override
    public Bitmap loadPodcastImage(String url) {
        return null;
    }
}
