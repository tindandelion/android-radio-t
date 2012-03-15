package org.dandelion.radiot.podcasts.download;

import android.content.Context;

import java.io.File;

public class RealPodcastDownloader implements PodcastDownloader {
    private PodcastDownloadManager downloadManager;
    private DownloadFolder destination;

    public RealPodcastDownloader(PodcastDownloadManager downloadManager, DownloadFolder destination) {
        this.downloadManager = downloadManager;
        this.destination = destination;
    }

    @Override
    public void downloadPodcast(Context context, String url) {
        destination.ensureExists();
        File dest = destination.makePathForUrl(url);
        downloadManager.submitRequest(url, dest);
    }
}
