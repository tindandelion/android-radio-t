package org.dandelion.radiot.podcasts.download;

public class DownloadStarter extends DownloadProcessor {
    private Downloader downloader;
    private DownloadFolder destination;

    public DownloadStarter(DownloadProcessor next, Downloader downloader, DownloadFolder destination) {
        super(next);
        this.downloader = downloader;
        this.destination = destination;
    }

    public void acceptTask(DownloadTask task) {
        destination.mkdirs();
        task.localPath = destination.makePathForUrl(task.url);
        task.id = downloader.submit(task);
        passFurther(task);
    }
}
