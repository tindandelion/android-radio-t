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

    public SystemDownloadMonitor(Context context) {
        this.context = context;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void start() {
        context.registerReceiver(onDownloadCompleted,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void stop() {
        context.unregisterReceiver(onDownloadCompleted);
    }
}
