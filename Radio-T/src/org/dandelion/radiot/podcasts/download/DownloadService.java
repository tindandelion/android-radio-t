package org.dandelion.radiot.podcasts.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.dandelion.radiot.podcasts.PodcastsApp;

public class DownloadService extends Service {
    private static String TAG = DownloadService.class.getName();
    public static String START_DOWNLOAD_ACTION = TAG + ".START_DOWNLOAD";
    public static final String DOWNLOAD_COMPLETE_ACTION = TAG + ".DOWNLOAD_COMPLETE";

    public static String URL_EXTRA = TAG + ".URL";
    public static final String TITLE_EXTRA = TAG + ".TITLE";
    public static final String TASK_ID_EXTRA = TAG + ".TASK_ID";

    private DownloadStarter downloader;
    private DownloadTracker tracker;
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (START_DOWNLOAD_ACTION.equals(action)) {
            startDownloading(intent);
        }
        if (DOWNLOAD_COMPLETE_ACTION.equals(action)) {
            processCompletedDownload(intent);
        }
        return START_STICKY;
    }

    private void processCompletedDownload(Intent intent) {
        long taskId = intent.getLongExtra(TASK_ID_EXTRA, 0);
        tracker.onDownloadComplete(taskId);
    }

    @Override
    public void onDestroy() {
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

    private void startDownloading(Intent intent) {
        DownloadTask task = new DownloadTask()
                .setUrl(intent.getStringExtra(URL_EXTRA))
                .setTitle(intent.getStringExtra(TITLE_EXTRA));
        downloader.acceptTask(task);
    }
    
    private void log(String message) {
        Log.v("DOWNLOAD", message);
    }
}
