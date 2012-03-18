package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SystemDownloadMonitor {
    public interface DownloadListener {
        void onDownloadComplete(long id);
    }

    private Context context;
    private DownloadListener downloadListener;
    private BroadcastReceiver onDownloadCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if (downloadListener != null) {
                downloadListener.onDownloadComplete(id);
            }
        }
    };

    private BroadcastReceiver onNotificationClicked = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    };

    public SystemDownloadMonitor(Context context) {
        this.context = context;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void start() {
        context.registerReceiver(onDownloadCompleted,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        context.registerReceiver(onNotificationClicked,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    public void stop() {
        context.unregisterReceiver(onDownloadCompleted);
        context.unregisterReceiver(onNotificationClicked);
    }
}
