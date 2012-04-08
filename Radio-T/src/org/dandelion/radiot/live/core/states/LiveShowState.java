package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

import java.io.Serializable;

public class LiveShowState implements Serializable {
	public void acceptVisitor(LiveShowPlayer.StateVisitor visitor, long timestamp) {
	}

    public void togglePlayback(LiveShowPlayer player) {
    }

    public void handleError(LiveShowPlayer player) {
    }
}
