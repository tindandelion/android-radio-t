package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.service.LiveShowClient;

public class LiveShowPresenter implements LiveShowPlayer.StateVisitor {

	private LiveShowActivity activity;

	public LiveShowPresenter(LiveShowActivity activity) {
		this.activity = activity;
	}

	public void onIdle() {
		beInactiveState();
	}

	public void onWaiting(long timestamp) {
        beActiveState(2, true, true, timestamp);
    }

	public void onConnecting(long timestamp) {
        beActiveState(1, false, true, timestamp);
    }

	public void onPlaying(long timestamp) {
        beActiveState(3, false, true, timestamp);
    }

	public void onStopping(long timestamp) {
        beActiveState(4, false, false, timestamp);
    }

    public void togglePlaybackState(LiveShowClient client) {
        client.togglePlayback();
	}

    private void beActiveState(int labelStringId, boolean isHelpTextVisible, boolean buttonEnabled, long timestamp) {
        activity.setStatusLabel(labelStringId);
        activity.showHelpText(isHelpTextVisible);
        activity.setButtonState(0, buttonEnabled);
        activity.startTimer(timestamp);
    }

    private void beInactiveState() {
		activity.setStatusLabel(0);
		activity.showHelpText(false);
		activity.setButtonState(1, true);
		activity.stopTimer();
	}
}
