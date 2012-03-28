package org.dandelion.radiot.live.service;

import android.app.Notification;
import org.dandelion.radiot.live.core.LiveShowPlayer;

public class NotificationController implements LiveShowPlayer.StateVisitor {
    private static final int PLAYING_ID = 0;
    private static final int CONNECTING_ID = 1;
    private static final int WAITING_ID = 2;

    private Foregrounder foregrounder;
    private NotificationBuilder notificationBuilder;
    private String[] stateLabels;

    public NotificationController(Foregrounder foregrounder, NotificationBuilder notificationBuilder, String[] stateLabels) {
        this.foregrounder = foregrounder;
        this.notificationBuilder = notificationBuilder;
        this.stateLabels = stateLabels;
    }

    @Override
    public void onWaiting(long timestamp) {
        startForeground(getStatusLabel(WAITING_ID));
    }

    @Override
    public void onIdle() {
        stopForeground();
    }

    @Override
    public void onConnecting(long timestamp) {
        startForeground(getStatusLabel(CONNECTING_ID));
    }

    @Override
    public void onPlaying(long timestamp) {
        startForeground(getStatusLabel(PLAYING_ID));
    }

    @Override
    public void onStopping(long timestamp) {
    }

    private String getStatusLabel(int id) {
        return stateLabels[id];
    }


    private void startForeground(String label) {
        Notification notification = notificationBuilder.createNotification(label);
        foregrounder.startForeground(notification);
    }

    private void stopForeground() {
        foregrounder.stopForeground();
    }
}
