package org.dandelion.radiot.podcasts.download;

import java.io.File;

class DownloadProcessor {

    private MediaScanner mediaScanner;
    private NotificationManager notificationManager;

    public DownloadProcessor(MediaScanner mediaScanner, NotificationManager notificationManager) {
        this.mediaScanner = mediaScanner;
        this.notificationManager = notificationManager;
    }

    public void downloadComplete(String title, File path) {
        mediaScanner.scanAudioFile(path);
        notificationManager.showSuccess(title, path);
    }

    public void downloadError(String title, int errorCode) {
        notificationManager.showError(title, errorCode);
    }
}
