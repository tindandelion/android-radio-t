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

	public void stopPlayback(LiveShowPlayer player) {
	}

    public void startPlayback(LiveShowPlayer player) {
	}

	public long getTimestamp() {
		return timestamp;
	}

    public void handleError(LiveShowPlayer player) {
    }
}
