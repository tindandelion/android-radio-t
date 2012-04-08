package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Waiting extends LiveShowState {

    @Override
    public void togglePlayback(LiveShowPlayer player) {
        player.beIdle();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
        visitor.onWaiting(timestamp);
    }
}
