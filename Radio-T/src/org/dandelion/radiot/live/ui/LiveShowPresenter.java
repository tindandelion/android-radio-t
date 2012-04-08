package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

public class LiveShowPresenter implements LiveShowPlayer.StateVisitor, LiveShowStateListener {

	private LiveShowActivity activity;

	public LiveShowPresenter(LiveShowActivity activity) {
		this.activity = activity;
	}

	public void onIdle() {
		beInactiveState();
	}

	public void onWaiting(long timestamp) {
        beActiveState(true, true, timestamp);
    }

	public void onConnecting(long timestamp) {
        beActiveState(false, true, timestamp);
    }

	public void onPlaying(long timestamp) {
        beActiveState(false, true, timestamp);
    }

	public void onStopping(long timestamp) {
        beActiveState(false, false, timestamp);
    }

    private void beActiveState(boolean isHelpTextVisible, boolean buttonEnabled, long timestamp) {
        activity.showHelpText(isHelpTextVisible);
        activity.setButtonState(0, buttonEnabled);
        activity.startTimer(timestamp);
    }

    private void beInactiveState() {
		activity.showHelpText(false);
		activity.setButtonState(1, true);
		activity.stopTimer();
	}

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        activity.setStatusLabel(state.ordinal());
        state.acceptVisitor(this, timestamp);
    }
}
