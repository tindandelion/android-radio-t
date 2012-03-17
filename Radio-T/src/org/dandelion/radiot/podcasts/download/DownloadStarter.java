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
        DownloadTask task = new DownloadTask(title, dest);
        long taskId = downloader.submitTask(url, task);
        tracker.taskScheduled(taskId, task);
    }
}
