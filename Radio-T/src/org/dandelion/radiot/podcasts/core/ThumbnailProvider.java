package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public interface ThumbnailProvider {
    Bitmap loadPodcastImage(String url);
}