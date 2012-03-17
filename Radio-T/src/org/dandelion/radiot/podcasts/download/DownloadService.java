package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import org.dandelion.radiot.podcasts.PodcastsApp;

public class DownloadService extends Service {
    private static String TAG = DownloadService.class.getName();
    public static String URL_EXTRA = TAG + ".Url";
    public static final String TITLE_EXTRA = TAG + ".Title";

    private DownloadStarter downloader;
    private DownloadTracker tracker;
    private BroadcastReceiver onDownloadCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            tracker.taskCompleted(id);
        }
    };
    private DownloadTracker.Listener onFinished = new DownloadTracker.Listener() {
        @Override
        public void onAllTasksCompleted() {
            stopSelf();
        }
    };


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        createCore();
        registerReceivers();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceivers();
        log("Service destroyed");
    }

    private void registerReceivers() {
        registerReceiver(onDownloadCompleted,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void createCore() {
        PodcastsApp app = PodcastsApp.getInstance();
        tracker = new DownloadTracker();
        tracker.setListener(onFinished);
        downloader = new DownloadStarter(app.createDownloadManager(),
                app.getPodcastDownloadFolder(), tracker);
    }

    private void unregisterReceivers() {
        unregisterReceiver(onDownloadCompleted);
    }

    private void handleCommand(Intent intent) {
        String url = intent.getStringExtra(URL_EXTRA);
        String title = intent.getStringExtra(TITLE_EXTRA);
        downloader.downloadPodcast(url, title);
    }
    
    private void log(String message) {
        Log.v("DOWNLOAD", message);
    }
}
