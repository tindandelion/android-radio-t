package org.dandelion.radiot.live.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class NotificationBuilder {
    private Context context;
    private String title;
    private int iconId;

    public NotificationBuilder(Context context, int iconId, String title) {
        this.iconId = iconId;
        this.title = title;
        this.context = context;
    }

    public Notification createNotification(String message) {
        Notification note = getNotification();
        note.setLatestEventInfo(context, title, message, getPendingIntent());
        return note;
    }

    private Notification getNotification() {
        return new Notification(iconId, null, System.currentTimeMillis());
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(context, 0,
                getIntent(), 0);
    }

    private Intent getIntent() {
        return new Intent(context, LiveShowActivity.class);
    }
}
