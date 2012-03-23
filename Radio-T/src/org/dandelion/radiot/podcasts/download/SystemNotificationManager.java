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
        Note note = new SuccessNote(context, title, audioFile);
        note.show(title, DOWNLOAD_COMPLETE_NOTE_ID);
    }

    @Override
    public void showError(String title) {
        Note note = new ErrorNote(context, title);
        note.show(title, DOWNLOAD_COMPLETE_NOTE_ID);
    }
}

abstract class Note {
    protected Context context;
    private String title;

    public Note(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    public void show(String tag, int id) {
        android.app.NotificationManager manager = (android.app.NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(tag, id, build());
    }

    private Notification build() {
        Notification note = new Notification(icon(),
                title, System.currentTimeMillis());
        note.setLatestEventInfo(context, title, text(), intent());
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        return note;
    }

    public abstract CharSequence text();
    public abstract int icon();
    public abstract PendingIntent intent();
}

class ErrorNote extends Note {
    ErrorNote(Context context, String title) {
        super(context, title);
    }

    @Override
    public CharSequence text() {
        return context.getString(R.string.download_error_message);
    }

    @Override
    public int icon() {
        return android.R.drawable.stat_sys_warning;
    }

    @Override
    public PendingIntent intent() {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}

class SuccessNote extends Note {
    private File path;

    SuccessNote(Context context, String title, File path) {
        super(context, title);
        this.path = path;
    }

    @Override
    public int icon() {
        return android.R.drawable.stat_sys_download_done;
    }

    @Override
    public CharSequence text() {
        return context.getString(R.string.download_complete_message);
    }

    @Override
    public PendingIntent intent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(path), "audio/mpeg");
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}

