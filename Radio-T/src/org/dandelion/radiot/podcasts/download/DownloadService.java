package org.dandelion.radiot.podcasts.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import org.dandelion.radiot.podcasts.PodcastsApp;

public class DownloadService extends Service {
    private static String TAG = DownloadService.class.getName();
    public static String URL_EXTRA = TAG + ".Url";
    private RealPodcastDownloader downloader;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        PodcastsApp app = PodcastsApp.getInstance();
        downloader = new RealPodcastDownloader(app.createDownloadManager(),
                app.getPodcastDownloadFolder());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast
                .makeText(this, "Service destroyed", Toast.LENGTH_LONG)
                .show();
    }

    private void handleCommand(Intent intent) {
        String url = intent.getStringExtra(URL_EXTRA);
        downloader.process(this, url);
    }
}
