package org.dandelion.radiot.podcasts.core;

public interface ThumbnailProvider {
    byte [] thumbnailDataFor(PodcastItem item);
}
