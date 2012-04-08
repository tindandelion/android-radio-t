package org.dandelion.radiot.live.service;

import android.app.NotificationManager;
import android.content.Context;

public class SystemNotificationBar implements NotificationBar {
    private Context context;

    public SystemNotificationBar(Context context) {
        this.context = context;
    }

    @Override
    public void showIcon(int id, int resourceId, String title, String text) {
        LiveShowNote note = new LiveShowNote(context, id);
        note
                .setTitle(title)
                .setText(text)
                .setIcon(resourceId)
                .show();
    }

    @Override
    public void hideIcon(int id) {
        noteManager().cancel(id);
    }

    private NotificationManager noteManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
