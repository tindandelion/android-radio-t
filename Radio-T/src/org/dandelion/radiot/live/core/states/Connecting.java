package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Connecting extends LiveShowState {
    @Override
    public void stopPlayback(LiveShowPlayer player) {
        player.beStopping();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onConnecting(this);
    }

    @Override
    public void handleError(LiveShowPlayer player) {
        player.beWaiting();
    }
}
