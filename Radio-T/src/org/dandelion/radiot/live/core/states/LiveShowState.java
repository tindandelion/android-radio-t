package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

import java.io.Serializable;

public class LiveShowState implements Serializable {
	private long timestamp;

    public LiveShowState() {
        this.timestamp = System.currentTimeMillis();
    }

	public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
	}

    public void togglePlayback(LiveShowPlayer player) {
    }

    public void handleError(LiveShowPlayer player) {
    }

    public long getTimestamp() {
		return timestamp;
	}
}
