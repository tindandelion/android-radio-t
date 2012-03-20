package org.dandelion.radiot.podcasts.download;

public class DownloadEngine {
    private DownloadManager downloadManager;
    private DownloadFolder destination;
    private MediaScanner mediaScanner;

    public DownloadEngine(DownloadManager downloadManager, DownloadFolder destination, MediaScanner mediaScanner) {
        this.downloadManager = downloadManager;
        this.destination = destination;
        this.mediaScanner = mediaScanner;
    }

    public void startDownloading(DownloadManager.DownloadTask task) {
        destination.mkdirs();
        task.localPath = destination.makePathForUrl(task.url);
        downloadManager.submit(task);
    }

    public void finishDownload(long id) {
        DownloadManager.DownloadTask completedTask = downloadManager.query(id);
        if (taskSuccessful(completedTask)) {
            mediaScanner.scanAudioFile(completedTask.localPath);
        }
    }

    private boolean taskSuccessful(DownloadManager.DownloadTask task) {
        return (task != null) && (task.isSuccessful);
    }
}
