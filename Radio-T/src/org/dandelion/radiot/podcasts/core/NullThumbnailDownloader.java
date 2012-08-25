package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public class NullThumbnailDownloader implements PodcastList.ThumbnailDownloader {
    @Override
    public Bitmap loadPodcastImage(PodcastItem item) {
        return null;
    }
}
