package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Playing extends PlaybackState {
    public Playing(PlaybackContext context) {
        super(context);
    }

    @Override
    public void enter() {
        getService().lockWifi();
        getService().goForeground(0);
    }

    @Override
    public void leave() {
        getService().unlockWifi();
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onPlaying(this);
    }
}
