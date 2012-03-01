package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Connecting extends PlaybackState {
    public Connecting(PlaybackContext context) {
        super(context);
    }

    @Override
    public void enter() {
        context.serviceGoForeground(1);
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onConnecting(this);
    }
}
