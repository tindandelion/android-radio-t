package org.dandelion.radiot.podcasts.core;

public class PodcastDownloader {
    private PodcastDownloadManager downloadManager;

    public PodcastDownloader(PodcastDownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public void downloadPodcast(PodcastItem item) {
        downloadManager.submitRequest(null, null);
    }
}
