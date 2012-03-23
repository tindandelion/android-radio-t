package org.dandelion.radiot.podcasts.download;

import org.dandelion.radiot.R;
import android.app.Notification;
import android.app.PendingIntent;
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
        android.app.NotificationManager manager = (android.app.NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification note = createNote(title, audioFile);
        manager.notify(DOWNLOAD_COMPLETE_NOTE_ID, note);
    }

    @Override
    public void showError(String title) {

    }

    private Notification createNote(String title, File path) {
        Notification note = new Notification(completionIcon(),
                title, System.currentTimeMillis());
        note.setLatestEventInfo(context, title, getContextText(), createIntent(path));
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        return note;
    }

    private int completionIcon() {
        return android.R.drawable.stat_sys_download_done;
    }

    private CharSequence getContextText() {
        return context.getString(R.string.download_complete_message);
    }

    private PendingIntent createIntent(File path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(path), "audio/mpeg");
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
