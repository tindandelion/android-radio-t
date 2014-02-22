package org.dandelion.radiot.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class IconNote {
    protected Context context;
    private int notificationId;
    private NotificationCompat.Builder builder;

    public IconNote(Context context, int notificationId) {
        this.context = context;
        this.notificationId = notificationId;
        this.builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true);
    }

    public IconNote setTitle(String value) {
        builder.setContentTitle(value);
        return this;
    }

    public IconNote setTicker(String value) {
        builder.setTicker(value);
        return this;
    }

    public IconNote setTitleAndTicker(String value) {
        setTitle(value);
        setTicker(value);
        return this;
    }

    public IconNote setText(String value) {
        builder.setContentText(value);
        return this;
    }

    public IconNote setIcon(int resourceId) {
        builder.setSmallIcon(resourceId);
        return this;
    }

    public IconNote beOngoing() {
        builder
                .setAutoCancel(false)
                .setOngoing(true);
        return this;
    }

    public IconNote performsAction(String action) {
        Intent intent = new Intent(action);
        return setContentIntent(intent);
    }


    public IconNote showsActivity(Class<LiveShowActivity> activityClass) {
        return setContentIntent(new Intent(context, activityClass));
    }

    public IconNote opensUri(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        return setContentIntent(intent);
    }

    public void show(String tag) {
        noteManager().notify(tag, notificationId, build());
    }

    public void show() {
        show(null);
    }

    public void hide() {
        noteManager().cancel(notificationId);
    }

    private NotificationManager noteManager() {
        return (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public Notification build() {
        return builder.build();
    }

    private IconNote setContentIntent(Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        return this;
    }

    public int id() {
        return notificationId;
    }

    public IconNote addAction(int icon, String title, PendingIntent intent) {
        builder.addAction(icon, title, intent);
        return this;
    }
}
