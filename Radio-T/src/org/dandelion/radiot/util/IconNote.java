package org.dandelion.radiot.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class IconNote {
    protected Context context;
    private String title;
    private int iconId;
    private String text;
    private int notificationId;
    private String ticker;
    private int flags = Notification.FLAG_AUTO_CANCEL;
    private Intent intent = new Intent();

    public IconNote(Context context, int notificationId) {
        this.context = context;
        this.notificationId = notificationId;
    }

    public IconNote setTitle(String title) {
        this.title = title;
        return this;
    }

    public IconNote setTicker(String value) {
        ticker = value;
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
        noteManager().notify(tag, notificationId, build());
    }

    private NotificationManager noteManager() {
        return (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show() {
        show(null);
    }

    public void hide() {
        noteManager().cancel(notificationId);
    }

    public Notification build() {
        Notification note = new Notification(iconId, ticker, System.currentTimeMillis());
        note.setLatestEventInfo(context, title, text, intent());
        note.flags |= flags;
        return note;
    }

    private PendingIntent intent() {
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    public int id() {
        return notificationId;
    }

    public IconNote beOngoing() {
        flags = Notification.FLAG_ONGOING_EVENT;
        return this;
    }

    public IconNote performsAction(String action) {
        intent = new Intent(action);
        return this;
    }

    public IconNote showsActivity(Class<LiveShowActivity> activityClass) {
        intent = new Intent(context, activityClass);
        return this;
    }

    public IconNote opensUri(Uri uri, String mimeType) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        return this;
    }

}
