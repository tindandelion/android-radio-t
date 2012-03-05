package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

public class Waiting extends LiveShowState {
    private static final int WAITING_NOTIFICATION_STRING_ID = 2;

    public Waiting(LiveShowPlayer context) {
        super(context);
    }

    @Override
    public void enter(ILiveShowService service) {
        service.goForeground(WAITING_NOTIFICATION_STRING_ID);
    }

    @Override
    public void leave(ILiveShowService service) {
    }

    @Override
    public void stopPlayback() {
        player.beIdle();
    }

    @Override
    public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        visitor.onWaiting(this);
    }
}
