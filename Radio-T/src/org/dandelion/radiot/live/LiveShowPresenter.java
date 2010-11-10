package org.dandelion.radiot.live;

import java.util.Timer;
import java.util.TimerTask;

import org.dandelion.radiot.live.LiveShowState.Connecting;
import org.dandelion.radiot.live.LiveShowState.ILiveShowVisitor;
import org.dandelion.radiot.live.LiveShowState.Idle;
import org.dandelion.radiot.live.LiveShowState.Playing;
import org.dandelion.radiot.live.LiveShowState.Stopping;
import org.dandelion.radiot.live.LiveShowState.Waiting;

public class LiveShowPresenter implements ILiveShowVisitor {

	private LiveShowActivity activity;
	private Timer timer;
	private boolean isActive;

	public LiveShowPresenter(LiveShowActivity activity) {
		this.activity = activity;
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
		activity.setElapsedTime(0);
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
		activity.setStatusLabel(labelStringId);
		activity.setButtonState(0, buttonEnabled);
		if (shouldShowWaitingHint) {
			activity.showWaitingHint();
		}
		restartTimer(state.getTimestamp());
	}

	private void beInactiveState() {
		isActive = false;
		activity.setStatusLabel(0);
		activity.setButtonState(1, true);
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
				updateTimerLabel(currentTime / 1000);
			}

			private void updateTimerLabel(final long seconds) {
				activity.runOnUiThread(new Runnable() {
					public void run() {
						activity.setElapsedTime(seconds);
					}
				});
			}
		};
	}
}
