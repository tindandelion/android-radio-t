package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowService.PlaybackState;

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
import android.widget.TextView;

public class LiveShowActivity extends Activity {

	// public static final String LIVE_SHOW_URL =
	// "http://stream3.radio-t.com:8181/stream";
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
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
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
		super.onStop();
	}

	public void onStartPlayback(View v) {
		service.startPlayback();
	}

	public void onStopPlayback(View v) {
		service.stopPlayback();
	}

	protected void updateVisualState() {
		TextView view = (TextView) findViewById(R.id.playback_state_label);
		view.setText(service.getState().toString());
	}
}
