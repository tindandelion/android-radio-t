package org.dandelion.radiot.live.service;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.util.IconNote;

public class ForegroundController implements LiveShowStateListener {
    private Foregrounder foregrounder;
    private IconNote note;

    public ForegroundController(Foregrounder foregrounder, IconNote note) {
        this.foregrounder = foregrounder;
        this.note = note;
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        if (LiveShowState.isIdle(state)) {
            foregrounder.stopForeground();
        } else {
            foregrounder.startForeground(note);
        }
    }
}
