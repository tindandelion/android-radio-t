package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

// TODO: Get rid of permanent reference to LiveShowPlayer
// TODO: Move functionality from enter() and leave()
public class LiveShowState {
    protected LiveShowPlayer player;
	private long timestamp;

    public LiveShowState(LiveShowPlayer player) {
        this.player = player;
        this.timestamp = System.currentTimeMillis();
    }

	public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
	}

	public void stopPlayback() {
        player.beStopping();
	}


    public void startPlayback() {
	}

	public long getTimestamp() {
		return timestamp;
	}

}
