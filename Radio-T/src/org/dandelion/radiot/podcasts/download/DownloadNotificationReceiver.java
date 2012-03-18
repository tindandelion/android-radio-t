package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
            openDownloadsActivity(context);
            return;
        }
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long taskId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            sendNotificationToDownloadService(context, taskId);
        }
    }

    private void sendNotificationToDownloadService(Context context, long taskId) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(DownloadService.DOWNLOAD_COMPLETE_ACTION);
        intent.putExtra(DownloadService.TASK_ID_EXTRA, taskId);
        context.startService(intent);
    }

    private void openDownloadsActivity(Context context) {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
