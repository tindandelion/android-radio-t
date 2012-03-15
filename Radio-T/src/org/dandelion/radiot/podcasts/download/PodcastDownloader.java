package org.dandelion.radiot.podcasts.download;

import java.io.File;

public class PodcastDownloader {
    private PodcastDownloadManager downloadManager;
    private DownloadFolder destination;

    public PodcastDownloader(PodcastDownloadManager downloadManager, DownloadFolder destination) {
        this.downloadManager = downloadManager;
        this.destination = destination;
    }

    public void downloadPodcast(String url) {
        destination.ensureExists();
        File dest = destination.makePathForUrl(url);
        downloadManager.submitRequest(url, dest);
    }

}
