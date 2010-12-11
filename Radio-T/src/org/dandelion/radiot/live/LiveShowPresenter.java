package org.dandelion.radiot.live;

import java.util.Timer;
import java.util.TimerTask;

import org.dandelion.radiot.live.LiveShowState.Connecting;
import org.dandelion.radiot.live.LiveShowState.ILiveShowVisitor;
import org.dandelion.radiot.live.LiveShowState.Idle;
import org.dandelion.radiot.live.LiveShowState.Playing;
import org.dandelion.radiot.live.LiveShowState.Stopping;
import org.dandelion.radiot.live.LiveShowState.Waiting;

interface ILiveShowPlaybackView {

	void setStatusLabel(int labelStringId);

	void setButtonState(int i, boolean buttonEnabled);

	void showWaitingHint();

	void setElapsedTime(long seconds);
	
}

public class LiveShowPresenter implements ILiveShowVisitor {

	private Timer timer;
	private boolean isActive;
	private ILiveShowPlaybackView view;

	public LiveShowPresenter(ILiveShowPlaybackView view) {
		this.view = view;
	}

	public void onIdle(Idle state) {
		beInactiveState();
	}

	public void onWaiting(Waiting state) {
		beActiveState(state, 2, true, true);
	}

	public void onConnecting(Connecting state) {
		beActiveState(state, 1, false, true);
	}

	public void onPlaying(Playing state) {
		beActiveState(state, 3, false, true);
	}

	public void onStopping(Stopping state) {
		beActiveState(state, 4, false, false);
	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		view.setElapsedTime(0);
	}

	public void switchPlaybackState(LiveShowState state) {
		if (isActive) {
			state.stopPlayback();
		} else {
			state.startPlayback();
		}
	}

	private void beActiveState(LiveShowState state, int labelStringId,
			boolean shouldShowWaitingHint, boolean buttonEnabled) {
		isActive = true;
		view.setStatusLabel(labelStringId);
		view.setButtonState(0, buttonEnabled);
		if (shouldShowWaitingHint) {
			view.showWaitingHint();
		}
		restartTimer(state.getTimestamp());
	}

	private void beInactiveState() {
		isActive = false;
		view.setStatusLabel(0);
		view.setButtonState(1, true);
		stopTimer();
	}

	private void restartTimer(long timestamp) {
		stopTimer();
		timer = new Timer();
		timer.schedule(createTimerTask(timestamp), 0, 1000);
	}

	private TimerTask createTimerTask(final long timestamp) {
		return new TimerTask() {
			public void run() {
				long currentTime = System.currentTimeMillis() - timestamp;
				view.setElapsedTime((currentTime / 1000));
			}
		};
	}
}
