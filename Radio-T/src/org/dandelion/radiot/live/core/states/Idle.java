package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Idle extends LiveShowState {

    @Override
    public void togglePlayback(LiveShowPlayer player) {
        player.beConnecting();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onIdle();
    }
}
