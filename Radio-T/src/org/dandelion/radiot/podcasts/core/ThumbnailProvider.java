package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public interface ThumbnailProvider {
    Bitmap thumbnailFor(PodcastItem item);
}
