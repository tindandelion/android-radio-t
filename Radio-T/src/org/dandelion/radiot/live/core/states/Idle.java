package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Idle extends PlaybackState {
    public Idle(PlaybackContext context) {
        super(context);
    }

    @Override
    public void enter() {
        getService().goBackground();
    }

    @Override
    public void startPlayback() {
        context.connect();
    }

    @Override
    public void stopPlayback() {
        // Do nothing
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onIdle(this);
    }
}
