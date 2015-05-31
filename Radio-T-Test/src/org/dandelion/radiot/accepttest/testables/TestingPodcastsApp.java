package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import org.dandelion.radiot.podcasts.main.PodcastsApp;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.download.DownloadManager;
import org.dandelion.radiot.podcasts.download.MediaScanner;
import org.dandelion.radiot.podcasts.download.NotificationManager;

import java.io.File;

public class TestingPodcastsApp extends PodcastsApp {
    private PodcastAction player;
    private DownloadManager downloadManager;
    private File downloadFolder;
    private MediaScanner mediaScanner;
    private NotificationManager notificationManager;

    public TestingPodcastsApp(Context context, PodcastAction player, DownloadManager downloadManager,
                              MediaScanner scanner, NotificationManager notificationManager) {
        super(context);
        this.player = player;
        this.downloadManager = downloadManager;
        this.mediaScanner = scanner;
        this.notificationManager = notificationManager;
    }

    @Override
    public PodcastAction createPlayer() {
        return player;
    }

    @Override
    public DownloadManager createDownloadManager() {
        return downloadManager;
    }

    @Override
    protected File getSystemDownloadFolder() {
        return downloadFolder;
    }

    @Override
    public MediaScanner createMediaScanner() {
        return mediaScanner;
    }

    @Override
    public NotificationManager createNotificationManager() {
        return notificationManager;
    }

    public void setDownloadFolder(File value) {
        downloadFolder = value;
    }


}
