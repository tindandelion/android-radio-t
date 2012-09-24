package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;

public class NullThumbnailProvider implements ThumbnailProvider {
    @Override
    public Bitmap thumbnailFor(PodcastItem item) {
        return null;
    }

    @Override
    public byte[] thumbnailDataFor(PodcastItem item) {
        return null;
    }
}
