package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Connecting extends LiveShowState {

    @Override
    public void togglePlayback(LiveShowPlayer player) {
        player.beStopping();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onConnecting(this.getTimestamp());
    }

    @Override
    public void handleError(LiveShowPlayer player) {
        player.beWaiting();
    }
}
