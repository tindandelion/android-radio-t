package org.dandelion.radiot.podcasts.download;

public interface PodcastDownloadManager {
    void submitRequest(String src, String dest);
}
