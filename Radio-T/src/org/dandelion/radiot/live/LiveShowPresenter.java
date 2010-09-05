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
			activity.setButtonText("Остановить");
		}

		@Override
		protected void updateStateLabel() {
			String text = "";
			if (showState instanceof LiveShowState.Connecting) {
				text = "Подключение";
			} else if (showState instanceof LiveShowState.Waiting) {
				text = "Ожидание";
			} else { 
				text = "Трансляция";
			}
			activity.setLabelText(text);
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
			activity.setButtonText("Подключиться");
		}

		@Override
		protected void updateStateLabel() {
			activity.setLabelText("Остановлено");
		}

		@Override
		protected void updateTimer() {
			activity.setTimerLabel("0:00");
		}
	}

}
