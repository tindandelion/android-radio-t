package org.dandelion.radiot.live.service;

import android.app.Notification;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.*;

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
    public void onWaiting(LiveShowState state) {
        startForeground(getStatusLabel(WAITING_ID));
    }

    @Override
    public void onIdle(LiveShowState state) {
        stopForeground();
    }

    @Override
    public void onConnecting(LiveShowState connecting) {
        startForeground(getStatusLabel(CONNECTING_ID));
    }

    @Override
    public void onPlaying(LiveShowState playing) {
        startForeground(getStatusLabel(PLAYING_ID));
    }

    @Override
    public void onStopping(LiveShowState stopping) {
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
