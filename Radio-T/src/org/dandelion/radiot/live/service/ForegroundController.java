package org.dandelion.radiot.live.service;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.util.IconNote;

public class ForegroundController implements LiveShowPlayer.StateVisitor {
    private Foregrounder foregrounder;
    private IconNote note;

    public ForegroundController(Foregrounder foregrounder, IconNote note) {
        this.foregrounder = foregrounder;
        this.note = note;
    }

    @Override
    public void onWaiting(long timestamp) {
        startForeground();
    }

    @Override
    public void onIdle() {
        stopForeground();
    }

    @Override
    public void onConnecting(long timestamp) {
        startForeground();
    }

    @Override
    public void onPlaying(long timestamp) {
        startForeground();
    }

    @Override
    public void onStopping(long timestamp) {
    }


    private void startForeground() {
        foregrounder.startForeground(note);
    }

    private void stopForeground() {
        foregrounder.stopForeground();
    }
}
