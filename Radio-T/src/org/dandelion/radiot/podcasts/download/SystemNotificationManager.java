package org.dandelion.radiot.podcasts.download;

import android.app.*;
import android.app.DownloadManager;
import org.dandelion.radiot.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class SystemNotificationManager implements NotificationManager {
    private int DOWNLOAD_COMPLETE_NOTE_ID = 2;
    private Context context;

    public SystemNotificationManager(Context context) {
        this.context = context;
    }

    @Override
    public void showSuccess(String title, File audioFile) {
        Notification note = createSuccessNote(title, audioFile);
        showNotification(title, note);
    }

    @Override
    public void showError(String title) {
        Notification note = createErrorNote(title);
        showNotification(title, note);
    }

    private void showNotification(String tag, Notification note) {
        android.app.NotificationManager manager = (android.app.NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(tag, DOWNLOAD_COMPLETE_NOTE_ID, note);
    }

    private Notification createErrorNote(String title) {
        Notification note = new Notification(errorIcon(),
                title, System.currentTimeMillis());
        note.setLatestEventInfo(context, title, errorText(), intentForShowingDownloads());
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        return note;
    }

    private CharSequence errorText() {
        return context.getString(R.string.download_error_message);
    }

    private int errorIcon() {
        return android.R.drawable.stat_sys_warning;
    }


    private Notification createSuccessNote(String title, File path) {
        Notification note = new Notification(completionIcon(),
                title, System.currentTimeMillis());
        note.setLatestEventInfo(context, title, completionText(), intentForPlayingFile(path));
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        return note;
    }

    private int completionIcon() {
        return android.R.drawable.stat_sys_download_done;
    }

    private PendingIntent intentForShowingDownloads() {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private CharSequence completionText() {
        return context.getString(R.string.download_complete_message);
    }

    private PendingIntent intentForPlayingFile(File path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(path), "audio/mpeg");
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
