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
		updateButton();
		updateStateLabel();
		updateTimer();
	}

	public void stop() {
	}

	protected void updateButton() {
	}

	protected void updateStateLabel() {
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
		protected void updateButton() {
			activity.setButtonLabel(0);
		}

		@Override
		protected void updateStateLabel() {
			int statusIndex = -1;
			if (showState instanceof LiveShowState.Connecting) {
				statusIndex = 1;
			} else if (showState instanceof LiveShowState.Waiting) {
				statusIndex = 2;
			} else { 
				statusIndex = 3;
			}
			activity.setStatusLabel(statusIndex);
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
					long seconds = currentTime / 1000;
					long minutes = seconds / 60;
					seconds = seconds % 60;
					updateTimerLabel(minutes, seconds);
				}
			};
		}

		private void updateTimerLabel(final long minutes, final long seconds) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					activity.setTimerLabel(String.format("%d:%02d", minutes,
							seconds));
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
		protected void updateButton() {
			activity.setButtonLabel(1);
		}

		@Override
		protected void updateStateLabel() {
			activity.setStatusLabel(0);
		}

		@Override
		protected void updateTimer() {
			activity.setTimerLabel("0:00");
		}
	}

}
