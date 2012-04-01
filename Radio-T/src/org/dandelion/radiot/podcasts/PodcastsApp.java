package org.dandelion.radiot.podcasts;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.download.*;
import org.dandelion.radiot.podcasts.core.PodcastAction;
import org.dandelion.radiot.podcasts.download.DownloadManager;
import org.dandelion.radiot.podcasts.download.NotificationManager;
import org.dandelion.radiot.podcasts.ui.ExternalPlayer;

import java.io.File;

public class PodcastsApp {
    private static PodcastsApp instance;
    protected Context context;

    public static void initialize(Context context) {
        if (null == instance) {
            instance = new PodcastsApp(context);
        }
    }
    
    public static void release() {
        instance.releaseInstance();
        instance = null;
    }

    public static PodcastsApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(PodcastsApp newInstance) {
        instance = newInstance;
    }

    protected PodcastsApp(Context context) {
        this.context = context;
    }

    private void releaseInstance() {
    }

    public PodcastAction createPlayer() {
        return new ExternalPlayer();
    }

    public PodcastAction createDownloader() {
        if (supportsDownload()) {
            return createDownloaderClient();
        } else {
            return fakeDownloader();
        }
    }

    protected boolean supportsDownload() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    private PodcastAction fakeDownloader() {
        return new FakeDownloader();
    }

    private PodcastAction createDownloaderClient() {
        return new DownloadServiceClient();
    }

    protected File getSystemDownloadFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    public DownloadManager createDownloadManager() {
        return new SystemDownloadManager(context);
    }

    public DownloadFolder getPodcastDownloadFolder() {
        return new DownloadFolder(getSystemDownloadFolder());
    }

    public MediaScanner createMediaScanner() {
        return new SystemMediaScanner(context);
    }

    public NotificationManager createNotificationManager() {
        return new DownloadNotifier(context, createDownloadErrorMessages());
    }

    private DownloadErrorMessages createDownloadErrorMessages() {
        return new DownloadErrorMessages(
                context.getResources().getStringArray(R.array.download_error_messages),
                android.app.DownloadManager.ERROR_UNKNOWN,
                context.getString(R.string.download_default_message)
        );
    }
}
