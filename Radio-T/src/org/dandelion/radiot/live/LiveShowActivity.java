package org.dandelion.radiot.live;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.dandelion.radiot.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LiveShowActivity extends Activity {
	public static HashMap<Class<?>, String> stateLabels = new HashMap<Class<?>, String>();
	static {
		stateLabels.put(LiveShowState.Idle.class, "Остановлено");
		stateLabels.put(LiveShowState.Waiting.class, "Ожидание");
		stateLabels.put(LiveShowState.Playing.class, "Трансляция");
	}

	class PlaybackTimer {
		private Timer timer;
		private long startTime;

		public void stop() {
			if (null != timer) {
				timer.cancel();
				timer = null;
				updateTimerLabel("0:00");
			}
		}

		public void restart(long time) {
			startTime = time;
			stop();
			timer = new Timer();
			timer.schedule(timerTask(), 0, 1000);
		}

		private TimerTask timerTask() {
			return new TimerTask() {
				@Override
				public void run() {
					long currentTime = System.currentTimeMillis() - startTime;
					long seconds = currentTime / 1000;
					long minutes = seconds / 60;
					seconds = seconds % 60;
					updateTimerLabel(String.format("%d:%02d", minutes, seconds));
				}
			};
		}
	}

	protected LiveShowService service;
	protected BroadcastReceiver onPlaybackState = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			updateVisualState();
		}
	};
	private ServiceConnection onService = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((LiveShowService.LocalBinder) binder).getService();
			registerReceiver(onPlaybackState, new IntentFilter(
					LiveShowService.PLAYBACK_STATE_CHANGED));
			updateVisualState();
			service.startPlayback();
		}
	};
	private PlaybackTimer pbTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		pbTimer = new PlaybackTimer();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent i = new Intent(this, LiveShowService.class);
		startService(i);
		bindService(i, onService, 0);
	}

	@Override
	protected void onStop() {
		unregisterReceiver(onPlaybackState);
		unbindService(onService);
		service = null;
		pbTimer.stop();
		super.onStop();
	}

	public void onButtonPressed(View v) {
		if (service.getCurrentState() instanceof LiveShowState.Idle) {
			service.startPlayback();
		} else {
			service.stopPlayback();
		}
	}

	protected void updateVisualState() {
		updateStateLabel(service.getCurrentState());
		updateButton(service.getCurrentState());
		restartTimer(service.getCurrentState());
	}

	private void restartTimer(LiveShowState state) {
		if (state instanceof LiveShowState.Idle) {
			pbTimer.stop();
		} else {
			pbTimer.restart(state.getTimestamp());
		}
	}

	private void updateButton(LiveShowState state) {
		Button button = (Button) findViewById(R.id.live_show_action_button);
		if (state instanceof LiveShowState.Idle) {
			button.setText("Подключиться");
		} else {
			button.setText("Остановить");
		}
	}

	private void updateStateLabel(LiveShowState state) {
		TextView view = (TextView) findViewById(R.id.playback_state_label);
		view.setText(stateLabels.get(state.getClass()));
	}

	private void updateTimerLabel(final String value) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				TextView timerLabel = (TextView) findViewById(R.id.live_timer_label);
				timerLabel.setText(value);
			}
		});
	}

	public LiveShowService getService() {
		return service;
	}
}
