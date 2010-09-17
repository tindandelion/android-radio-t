package org.dandelion.radiot.live;

import java.util.Timer;
import java.util.TimerTask;

public class LiveShowPresenter {
	public static final LiveShowPresenter Null = new LiveShowPresenter(null,
			null);

	protected LiveShowActivity activity;
	protected LiveShowState showState;

	private LiveShowPresenter(LiveShowState state, LiveShowActivity activity) {
		this.showState = state;
		this.activity = activity;
	}

	public static LiveShowPresenter create(LiveShowState state,
			LiveShowActivity activity) {
		if (state instanceof LiveShowState.Idle) {
			return new InactiveStatePresenter(state, activity);
		} else {
			return new ActiveStatePresenter(state, activity);
		}
	}

	public void switchPlaybackState() {
	}

	public void updateView() {
		updateTimer();
	}

	public void stop() {
	}

	protected void updateTimer() {
	}

	static class ActiveStatePresenter extends LiveShowPresenter {
		private Timer timer;

		public ActiveStatePresenter(LiveShowState state,
				LiveShowActivity activity) {
			super(state, activity);
		}

		@Override
		public void switchPlaybackState() {
			showState.stopPlayback();
		}

		@Override
		protected void updateTimer() {
			timer = new Timer(true);
			timer.schedule(createTimerTask(), 0, 1000);
		}

		private TimerTask createTimerTask() {
			return new TimerTask() {
				public void run() {
					long currentTime = System.currentTimeMillis()
							- showState.getTimestamp();
					updateTimerLabel(currentTime / 1000);
				}
			};
		}
		
		private void updateTimerLabel(final long seconds) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					activity.setElapsedTime(seconds);
				}
			});
		}

		@Override
		public void stop() {
			timer.cancel();
		}
	}

	static class InactiveStatePresenter extends LiveShowPresenter {
		public InactiveStatePresenter(LiveShowState state,
				LiveShowActivity activity) {
			super(state, activity);
		}

		@Override
		public void switchPlaybackState() {
			showState.startPlayback();
		}

		@Override
		protected void updateTimer() {
			activity.setElapsedTime(0);
		}
	}
}
