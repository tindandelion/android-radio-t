package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Waiting extends LiveShowState {
    public Waiting(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void stopPlayback() {
        player.beIdle();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onWaiting(this);
    }
}
