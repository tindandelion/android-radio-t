package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Playing extends LiveShowState {
    public Playing(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onPlaying(this);
    }
}
