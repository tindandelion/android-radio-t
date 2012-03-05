package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;

// TODO: Get rid of permanent reference to LiveShowPlayer
// TODO: Move functionality from enter() and leave()
public class PlaybackState {
    protected LiveShowPlayer player;
	private long timestamp;

    public interface ILiveShowService {
		void goForeground(int stringId);

		void goBackground();

		void lockWifi();

		void unlockWifi();
    }

    public PlaybackState(LiveShowPlayer player) {
        this.player = player;
        this.timestamp = System.currentTimeMillis();
    }

    public void enter(ILiveShowService service) {
	}

	public void leave(ILiveShowService service) {
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
