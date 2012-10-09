package org.dandelion.radiot.podcasts.loader;

// TODO: This is no longer a thumbnail provider, just a data fetcher
public interface ThumbnailProvider {
    byte [] thumbnailDataFor(String url);

}
