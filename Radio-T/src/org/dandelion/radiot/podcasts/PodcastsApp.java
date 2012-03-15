package org.dandelion.radiot.podcasts;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import org.dandelion.radiot.podcasts.download.*;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;
import org.dandelion.radiot.podcasts.ui.ExternalPlayer;

import java.io.File;

public class PodcastsApp {
    private static PodcastsApp instance;
    private Context context;

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

    public PodcastProcessor createPlayer() {
        return new ExternalPlayer();
    }

    public PodcastProcessor createDownloader() {
        if (supportsDownload()) {
            return createRealDownloader();
        } else {
            return fakeDownloader();
        }
    }

    protected boolean supportsDownload() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    private PodcastProcessor fakeDownloader() {
        return new FakePodcastDownloader();
    }

    private PodcastProcessor createRealDownloader() {
        return new RealPodcastDownloader(createDownloadManager(),
                getPodcastDownloadFolder());
    }

    protected File getSystemDownloadFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    protected PodcastDownloadManager createDownloadManager() {
        return new SystemDownloadManager((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE));
    }

    public DownloadFolder getPodcastDownloadFolder() {
        return new DownloadFolder(getSystemDownloadFolder());
    }
}
