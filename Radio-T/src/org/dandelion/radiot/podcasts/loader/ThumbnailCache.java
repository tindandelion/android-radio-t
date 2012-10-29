package org.dandelion.radiot.podcasts.loader;

public interface ThumbnailCache {
    void update(String url, byte[] thumbnail);
    byte[] lookup(String url);
}
