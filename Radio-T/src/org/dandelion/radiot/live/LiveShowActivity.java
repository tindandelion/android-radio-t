package org.dandelion.radiot.live;

import org.dandelion.radiot.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LiveShowActivity extends Activity implements
		LiveShowService.ILivePlaybackView {

	// public static final String LIVE_SHOW_URL =
	// "http://stream3.radio-t.com:8181/stream";
	public static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";

	private ToggleButton playbackButton;
	protected LiveShowService liveService;

	private ServiceConnection onService = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
			liveService = null;
		}

		public void onServiceConnected(ComponentName name, IBinder binder) {
			liveService = ((LiveShowService.LocalBinder) binder).getService();
			liveService.attach(LiveShowActivity.this);
			liveService.startPlaying(LIVE_SHOW_URL);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		playbackButton = (ToggleButton) findViewById(R.id.live_show_playback_button);
		bindToService();
	}

	private void bindToService() {
		Log.i("RadioT", "Activity is binding to service");
		Intent intent = new Intent(this, LiveShowService.class);
		startService(intent);
		bindService(intent, onService, 0); 
	}
	
	@Override
	protected void onDestroy() {
		liveService.detach();
		Log.i("RadioT", "Activity unbinds from service: " + onService);
		unbindService(onService);
		super.onStop();
	}

	public void toggleLiveShow(View v) {
		liveService.togglePlaying(playbackButton.isChecked());
	}

	public void enableControls(boolean enabled) {
		playbackButton.setEnabled(enabled);
	}

	public void setPlaying(boolean playing) {
		playbackButton.setChecked(playing);
	}

	public void showPlaybackError() {
		Toast.makeText(this, getString(R.string.live_show_playback_error),
				Toast.LENGTH_LONG).show();
	}
}
