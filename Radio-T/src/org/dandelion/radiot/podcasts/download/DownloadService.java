package org.dandelion.radiot.podcasts.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.dandelion.radiot.podcasts.PodcastsApp;

public class DownloadService extends Service {
    private static String TAG = DownloadService.class.getName();
    public static String URL_EXTRA = TAG + ".Url";
    public static final String TITLE_EXTRA = TAG + ".Title";

    private DownloadStarter downloader;
    private DownloadTracker tracker;
    private SystemDownloadMonitor monitor;
    private DownloadProcessor localFileProcessor;

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
        createMonitor();
    }

    private void createMonitor() {
        monitor = new SystemDownloadMonitor(this);
        monitor.setDownloadListener(tracker);
        monitor.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        monitor.stop();
        log("Service destroyed");
    }

    private void createCore() {
        PodcastsApp app = PodcastsApp.getInstance();
        localFileProcessor = new MediaScannerProcessor(app.createMediaScanner());
        tracker = new DownloadTracker(localFileProcessor);
        tracker.setListener(onFinished);
        downloader = new DownloadStarter(tracker, app.createDownloadManager(),
                app.getPodcastDownloadFolder());
    }

    private void handleCommand(Intent intent) {
        DownloadTask task = new DownloadTask()
                .setUrl(intent.getStringExtra(URL_EXTRA))
                .setTitle(intent.getStringExtra(TITLE_EXTRA));
        downloader.acceptTask(task);
    }
    
    private void log(String message) {
        Log.v("DOWNLOAD", message);
    }
}
