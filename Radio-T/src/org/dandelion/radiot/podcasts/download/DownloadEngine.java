package org.dandelion.radiot.podcasts.download;

public class DownloadEngine {
    private DownloadManager downloadManager;
    private DownloadFolder destination;
    private MediaScanner mediaScanner;
    private NotificationManager notificationManager;

    public DownloadEngine(DownloadManager downloadManager, DownloadFolder destination, MediaScanner mediaScanner,
                          NotificationManager notificationManager) {
        this.downloadManager = downloadManager;
        this.destination = destination;
        this.mediaScanner = mediaScanner;
        this.notificationManager = notificationManager;
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
            notificationManager.showNotification(completedTask.title, completedTask.localPath);
        }
    }

    private boolean taskSuccessful(DownloadManager.DownloadTask task) {
        return (task != null) && (task.isSuccessful);
    }
}
