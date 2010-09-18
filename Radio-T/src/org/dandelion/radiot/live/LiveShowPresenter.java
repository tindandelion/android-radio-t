package org.dandelion.radiot.live;

import java.util.Timer;
import java.util.TimerTask;

import org.dandelion.radiot.live.LiveShowState.Connecting;
import org.dandelion.radiot.live.LiveShowState.ILiveShowVisitor;
import org.dandelion.radiot.live.LiveShowState.Idle;
import org.dandelion.radiot.live.LiveShowState.Playing;
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
		beActiveState(state, 2, true);
	}


	public void onConnecting(Connecting state) {
		beActiveState(state, 1, false);
	}

	public void onPlaying(Playing state) {
		beActiveState(state, 3, false);
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
			boolean isHelpTextVisible) {
		isActive = true;
		activity.setStatusLabel(labelStringId);
		activity.showHelpText(isHelpTextVisible);
		activity.setButtonLabel(0);
		restartTimer(state.getTimestamp());
	}
	
	private void beInactiveState() {
		isActive = false;
		activity.setStatusLabel(0);
		activity.showHelpText(false);
		activity.setButtonLabel(1);
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
