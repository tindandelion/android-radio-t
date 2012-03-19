package org.dandelion.radiot.podcasts.download;

public class DownloadTracker extends DownloadProcessor {
    private Downloader downloader;

    public DownloadTracker(DownloadProcessor next, Downloader downloader) {
        super(next);
        this.downloader = downloader;
    }

    public void acceptTask(DownloadTask task) {
    }

    public void onDownloadComplete(long id) {
        DownloadTask completedTask = downloader.query(id);
        if (completedTask != null) {
            passFurther(completedTask);
        }
    }

}
