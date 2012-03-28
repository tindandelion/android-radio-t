package org.dandelion.radiot.live.ui;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.*;
import org.dandelion.radiot.live.service.LiveShowService;

public class LiveShowPresenter implements LiveShowPlayer.StateVisitor {

	private LiveShowActivity activity;

	public LiveShowPresenter(LiveShowActivity activity) {
		this.activity = activity;
	}

	public void onIdle(LiveShowState state) {
		beInactiveState();
	}

	public void onWaiting(LiveShowState state) {
		beActiveState(state, 2, true, true);
	}

	public void onConnecting(LiveShowState state) {
		beActiveState(state, 1, false, true);
	}

	public void onPlaying(LiveShowState state) {
		beActiveState(state, 3, false, true);
	}

	public void onStopping(LiveShowState state) {
		beActiveState(state, 4, false, false);
	}

    public void togglePlaybackState(LiveShowService service) {
        service.togglePlayback();
	}

	private void beActiveState(LiveShowState state, int labelStringId,
			boolean isHelpTextVisible, boolean buttonEnabled) {
		activity.setStatusLabel(labelStringId);
		activity.showHelpText(isHelpTextVisible);
		activity.setButtonState(0, buttonEnabled);
        activity.startTimer(state.getTimestamp());
	}

	private void beInactiveState() {
		activity.setStatusLabel(0);
		activity.showHelpText(false);
		activity.setButtonState(1, true);
		activity.stopTimer();
	}
}
