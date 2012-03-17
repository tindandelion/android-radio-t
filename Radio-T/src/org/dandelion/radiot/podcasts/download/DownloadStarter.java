package org.dandelion.radiot.podcasts.download;

import java.io.File;

public class DownloadStarter {
    private DownloadTracker tracker;
    private Downloader downloader;
    private DownloadFolder destination;

    public DownloadStarter(Downloader downloader, DownloadFolder destination, DownloadTracker tracker) {
        this.downloader = downloader;
        this.destination = destination;
        this.tracker = tracker;
    }

    public void downloadPodcast(String url, String title) {
        destination.ensureExists();
        File dest = destination.makePathForUrl(url);
        long taskId = downloader.submitRequest(url, dest);
        tracker.taskScheduled(taskId);
    }
}
