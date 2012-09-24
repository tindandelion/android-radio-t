package org.dandelion.radiot.podcasts.core;

public class NullThumbnailProvider implements ThumbnailProvider {

    @Override
    public byte[] thumbnailDataFor(PodcastItem item) {
        return null;
    }
}
