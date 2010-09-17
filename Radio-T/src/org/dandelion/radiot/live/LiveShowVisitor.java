package org.dandelion.radiot.live;

import java.util.Timer;
import java.util.TimerTask;

import org.dandelion.radiot.live.LiveShowState.Connecting;
import org.dandelion.radiot.live.LiveShowState.ILiveShowVisitor;
import org.dandelion.radiot.live.LiveShowState.Idle;
import org.dandelion.radiot.live.LiveShowState.Playing;
import org.dandelion.radiot.live.LiveShowState.Waiting;

public class LiveShowVisitor implements ILiveShowVisitor {

	private LiveShowActivity activity;
	private Timer timer;

	public LiveShowVisitor(LiveShowActivity activity) {
		this.activity = activity;
	}

	public void onIdle(Idle state) {
		setStateLabel(0);
		showHelpText(false);
		beInactiveState();
	}

	public void onWaiting(Waiting state) {
		setStateLabel(2);
		showHelpText(true);
		beActiveState(state.getTimestamp());
	}

	public void onConnecting(Connecting state) {
		setStateLabel(1);
		showHelpText(false);
		beActiveState(state.getTimestamp());
	}

	public void onPlaying(Playing state) {
		setStateLabel(3);
		showHelpText(false);
		beActiveState(state.getTimestamp());
	}

	private void setStateLabel(int stringId) {
		activity.setStatusLabel(stringId);
	}

	private void setButtonLabel(int stringId) {
		activity.setButtonLabel(stringId);
	}

	private void showHelpText(boolean b) {
		activity.showHelpText(b);
	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		activity.setElapsedTime(0);
	}

	private void beActiveState(long timestamp) {
		setButtonLabel(0);
		restartTimer(timestamp);
	}
	
	private void beInactiveState() {
		setButtonLabel(1);
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
