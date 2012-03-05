package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Stopping extends PlaybackState {
    public Stopping(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void stopPlayback() {
    }

    @Override
    public void startPlayback() {
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onStopping(this);
    }
}
