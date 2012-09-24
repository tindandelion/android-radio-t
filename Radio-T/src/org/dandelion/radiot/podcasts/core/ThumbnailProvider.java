package org.dandelion.radiot.podcasts.core;

// TODO: This is no longer a thumbnail provider, just a data fetcher
public interface ThumbnailProvider {
    byte [] thumbnailDataFor(String url);
}
