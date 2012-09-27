package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import android.content.res.AssetManager;
import org.dandelion.radiot.helpers.FakeCache;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.download.DownloadManager;
import org.dandelion.radiot.podcasts.download.MediaScanner;
import org.dandelion.radiot.podcasts.download.NotificationManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TestingPodcastsApp extends PodcastsApp {
    private PodcastAction player;
    private DownloadManager downloadManager;
    private boolean downloadSupported = true;
    private File downloadFolder;
    private MediaScanner mediaScanner;
    private NotificationManager notificationManager;
    private AssetManager assets;

    public TestingPodcastsApp(Context context, PodcastAction player, DownloadManager downloadManager,
                              MediaScanner scanner, NotificationManager notificationManager) {
        super(context);
        this.player = player;
        this.downloadManager = downloadManager;
        this.mediaScanner = scanner;
        this.notificationManager = notificationManager;
        this.assets = context.getAssets();
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

    @Override
    protected boolean supportsDownload() {
        return downloadSupported;
    }

    @Override
    public PodcastListLoader createLoaderForShow(String name) {
        return new AsyncPodcastListLoader(createTestProvider(), new FakeCache());
    }

    private PodcastsProvider createTestProvider() {
        String address = null;
        return new RssFeedProvider(address, ThumbnailProvider.Null) {
            @Override
            protected InputStream openContentStream() throws IOException {
                return assets.open("radio-t.xml");
            }
        };
    }

    public void setDownloadSupported(boolean value) {
        downloadSupported = value;
    }

    public void setDownloadFolder(File value) {
        downloadFolder = value;
    }


}
