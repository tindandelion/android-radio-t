package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Stopping extends PlaybackState {
    public Stopping(PlaybackContext context) {
        super(context);
    }

    @Override
    public void stopPlayback() {
    }

    @Override
    public void startPlayback() {
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onStopping(this);
    }
}
