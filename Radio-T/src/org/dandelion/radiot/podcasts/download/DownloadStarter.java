package org.dandelion.radiot.podcasts.download;

public class DownloadStarter {
    private Downloader downloader;
    private DownloadFolder destination;

    public DownloadStarter(Downloader downloader, DownloadFolder destination) {
        this.downloader = downloader;
        this.destination = destination;
    }

    public void startDownloading(DownloadTask task) {
        destination.mkdirs();
        task.localPath = destination.makePathForUrl(task.url);
        downloader.submit(task);
    }
}
