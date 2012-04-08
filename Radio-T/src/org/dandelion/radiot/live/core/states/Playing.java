package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Playing extends LiveShowState {

    @Override
    public void togglePlayback(LiveShowPlayer player) {
        player.beStopping();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
        visitor.onPlaying(timestamp);
    }

    public void handleError(LiveShowPlayer player) {
        player.beConnecting();
    }
}
