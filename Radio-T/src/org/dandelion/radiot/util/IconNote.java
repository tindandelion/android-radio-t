package org.dandelion.radiot.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public abstract class IconNote {
    protected Context context;
    private String title;
    private String text;
    private int iconId;
    private int notificationId;
    private String ticker;

    public IconNote(Context context, int notificationId) {
        this.context = context;
        this.notificationId = notificationId;
    }

    public IconNote setTitle(String title) {
        this.title = title;
        return this;
    }

    public IconNote setTitleAndTicker(String value) {
        this.title = value;
        this.ticker = value;
        return this;
    }

    public IconNote setText(String value) {
        this.text = value;
        return this;
    }

    public IconNote setIcon(int resourceId) {
        this.iconId = resourceId;
        return this;
    }

    public void show(String tag) {
        android.app.NotificationManager manager = (android.app.NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(tag, notificationId, build());
    }

    public Notification build() {
        Notification note = new Notification(iconId, ticker, System.currentTimeMillis());
        note.setLatestEventInfo(context, title, text, intent());
        note.flags |= Notification.FLAG_AUTO_CANCEL;
        return note;
    }

    private PendingIntent intent() {
        return PendingIntent.getActivity(context, 0, activityIntent(), 0);
    }

    protected abstract Intent activityIntent();

    public int id() {
        return notificationId;
    }
}
