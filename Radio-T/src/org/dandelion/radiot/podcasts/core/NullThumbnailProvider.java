package org.dandelion.radiot.podcasts.core;

public class NullThumbnailProvider implements ThumbnailProvider {
    @Override
    public byte[] thumbnailDataFor(String url) {
        return null;
    }
}
