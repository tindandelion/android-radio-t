package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Playing extends PlaybackState {
    public Playing(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void enter(ILiveShowService service) {
        service.lockWifi();
        service.goForeground(0);
    }

    @Override
    public void leave(ILiveShowService service) {
        service.unlockWifi();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.PlaybackStateVisitor visitor) {
        visitor.onPlaying(this);
    }
}
