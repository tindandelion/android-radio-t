package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Idle extends BasicState {
    public Idle(PlaybackContext context) {
        super(context);
        getPlayer().setOnErrorListener(null);
    }

    @Override
    public void enter() {
        getService().goBackground();
    }

    @Override
    public void startPlayback() {
        getService().switchToNewState(newConnecting());
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onIdle(this);
    }
}
