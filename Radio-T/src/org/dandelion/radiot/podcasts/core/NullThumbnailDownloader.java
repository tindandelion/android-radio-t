package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public class NullThumbnailDownloader implements ThumbnailDownloader {

    @Override
    public Bitmap loadPodcastImage(String url) {
        return null;
    }
}
