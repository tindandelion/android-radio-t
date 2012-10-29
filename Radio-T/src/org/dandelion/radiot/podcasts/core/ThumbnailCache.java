package org.dandelion.radiot.podcasts.core;

public interface ThumbnailCache {
    void update(String url, byte[] thumbnail);
    byte[] lookup(String url);
}
