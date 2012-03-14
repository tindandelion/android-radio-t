package org.dandelion.radiot.podcasts.core;

public class SystemDownloadManager implements PodcastDownloadManager {
    @Override
    public void submitRequest(String src, String dest) {
        throw new RuntimeException("Not implemented");
    }
}
