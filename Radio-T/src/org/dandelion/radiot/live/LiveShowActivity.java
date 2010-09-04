package org.dandelion.radiot.live;

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
	private LiveShowPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		presenter = LiveShowPresenter.Null;
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
		presenter.stop();
		super.onStop();
	}

	public void onButtonPressed(View v) {
		presenter.switchPlaybackState();
	}

	protected void updateVisualState() {
		presenter.stop();
		presenter = LiveShowPresenter.create(service.getCurrentState(), this);
		presenter.updateView();
	}

	public LiveShowService getService() {
		return service;
	}

	public void setButtonText(String text) {
		Button button = (Button) findViewById(R.id.live_show_action_button);
		button.setText(text);
	}

	public void setLabelText(String text) {
		TextView view = (TextView) findViewById(R.id.playback_state_label);
		view.setText(text);
	}

	public void setTimerLabel(String string) {
		TextView timerLabel = (TextView) findViewById(R.id.live_timer_label);
		timerLabel.setText(string);
	}
}
