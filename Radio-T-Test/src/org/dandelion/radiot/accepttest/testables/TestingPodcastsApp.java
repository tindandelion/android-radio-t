package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.download.DownloadManager;
import org.dandelion.radiot.podcasts.download.MediaScanner;
import org.dandelion.radiot.podcasts.download.NotificationManager;
import org.dandelion.radiot.podcasts.loader.PodcastListLoader;
import org.dandelion.radiot.podcasts.loader.PodcastsConsumer;
import org.dandelion.radiot.podcasts.loader.ProgressListener;

import java.io.File;

public class TestingPodcastsApp extends PodcastsApp {
    private PodcastAction player;
    private DownloadManager downloadManager;
    private boolean downloadSupported = true;
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

    @Override
    protected boolean supportsDownload() {
        return downloadSupported;
    }

    @Override
    public PodcastListLoader createLoaderForShow(String name) {
        return new PodcastListLoader() {
            @Override
            public void refresh(boolean resetCache) {
            }

            @Override
            public void cancelUpdate() {
            }

            @Override
            public void detach() {
            }

            @Override
            public void attach(ProgressListener listener, PodcastsConsumer consumer) {
                PodcastList list = new PodcastList();
                PodcastItem item = new PodcastItem();

                item.setAudioUri("http://example.com/podcast.mp3");
                list.add(item);
                consumer.updatePodcasts(list);
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
