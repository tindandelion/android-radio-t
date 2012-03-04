package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.PlaybackContext;

public class Waiting extends PlaybackState {
    private static final int WAITING_NOTIFICATION_STRING_ID = 2;

    public Waiting(PlaybackContext context) {
        super(context);
    }

    @Override
    public void enter() {
        getService().goForeground(WAITING_NOTIFICATION_STRING_ID);
    }

    @Override
    public void leave() {
    }

    @Override
    public void stopPlayback() {
        context.beIdle();
    }

    @Override
    public void acceptVisitor(PlaybackContext.PlaybackStateVisitor visitor) {
        visitor.onWaiting(this);
    }
}
