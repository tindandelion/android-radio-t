package org.dandelion.radiot.live.service;

import android.app.Notification;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.*;

// TODO: Notification ID must be moved to Foregrounder?
public class NotificationController implements LiveShowPlayer.StateVisitor {
    private static final int PLAYING_ID = 0;
    private static final int CONNECTING_ID = 1;
    private static final int WAITING_ID = 2;

    private Foregrounder foregrounder;
    private NotificationBuilder notificationBuilder;
    private int notificationId;
    private String[] statusLabels;

    public NotificationController(int notificationId, Foregrounder foregrounder, NotificationBuilder notificationBuilder, String[] statusLabels) {
        this.foregrounder = foregrounder;
        this.notificationBuilder = notificationBuilder;
        this.notificationId = notificationId;
        this.statusLabels = statusLabels;
    }

    @Override
    public void onWaiting(Waiting state) {
        startForeground(getStatusLabel(WAITING_ID));
    }

    @Override
    public void onIdle(Idle state) {
        stopForeground();
    }

    @Override
    public void onConnecting(Connecting connecting) {
        startForeground(getStatusLabel(CONNECTING_ID));
    }

    @Override
    public void onPlaying(Playing playing) {
        startForeground(getStatusLabel(PLAYING_ID));
    }


    @Override
    public void onStopping(Stopping stopping) {
    }

    private String getStatusLabel(int id) {
        return statusLabels[id];
    }

    private void startForeground(String label) {
        Notification notification = notificationBuilder.createNotification(label);
        foregrounder.startForeground(notificationId, notification);
    }

    private void stopForeground() {
        foregrounder.stopForeground();
    }
}
