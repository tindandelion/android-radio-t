package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public interface ThumbnailDownloader {
    Bitmap loadPodcastImage(String url);
}
