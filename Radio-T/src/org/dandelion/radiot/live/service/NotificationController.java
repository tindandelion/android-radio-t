package org.dandelion.radiot.live.service;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.util.IconNote;

public class NotificationController implements LiveShowPlayer.StateVisitor {
    private static final int PLAYING_ID = 0;
    private static final int CONNECTING_ID = 1;
    private static final int WAITING_ID = 2;

    private Foregrounder foregrounder;
    private String[] stateLabels;
    private IconNote note;

    public NotificationController(Foregrounder foregrounder, String[] stateLabels, IconNote note) {
        this.foregrounder = foregrounder;
        this.stateLabels = stateLabels;
        this.note = note;
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
        note.setText(label);
        foregrounder.startForeground(note);
    }

    private void stopForeground() {
        foregrounder.stopForeground();
    }
}
