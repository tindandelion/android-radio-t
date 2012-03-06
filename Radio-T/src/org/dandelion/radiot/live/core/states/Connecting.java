package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Connecting extends LiveShowState {
    public Connecting(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onConnecting(this);
    }
}
