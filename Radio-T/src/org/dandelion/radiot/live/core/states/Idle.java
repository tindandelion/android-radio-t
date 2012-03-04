package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Idle extends PlaybackState {
    public Idle(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void enter(ILiveShowService service) {
        service.goBackground();
    }

    @Override
    public void startPlayback() {
        player.beConnecting();
    }

    @Override
    public void stopPlayback() {
        // Do nothing
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.PlaybackStateVisitor visitor) {
        visitor.onIdle(this);
    }
}
