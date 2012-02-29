package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Stopping extends BasicState implements Runnable {
    public Stopping(PlaybackContext context) {
        super(context);
    }

    @Override
    public void enter() {
        getService().runAsynchronously(this);
    }

    @Override
    public void stopPlayback() {
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onStopping(this);
    }

    public void run() {
        getPlayer().reset();
        getService().switchToNewState(newIdle());
    }

}
