package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;

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
		LiveShowPlaybackController.ILivePlaybackView {

	// public static final String LIVE_SHOW_URL =
	// "http://stream3.radio-t.com:8181/stream";
	public static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";

	private LiveShowPlaybackController playbackController;
	private ToggleButton playbackButton;

	protected LiveShowService liveService;

	private ServiceConnection onService = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
			liveService = null;
		}

		public void onServiceConnected(ComponentName name, IBinder binder) {
			liveService = ((LiveShowService.LocalBinder) binder).getService();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		playbackButton = (ToggleButton) findViewById(R.id.live_show_playback_button);
		createPlaybackController();
		bindToService();
	}

	private void bindToService() {
		bindService(new Intent(this, LiveShowService.class), onService,
				BIND_AUTO_CREATE);
	}

	protected void createPlaybackController() {
		Object[] lastState = (Object[]) getLastNonConfigurationInstance();
		if (null != lastState) {
			playbackController = (LiveShowPlaybackController) lastState[0];
		}
		if (null == playbackController) {
			playbackController = new LiveShowPlaybackController(
					((RadiotApplication) getApplication()).getMediaPlayer());
		}
		playbackController.attach(this);
	}

	@Override
	protected void onResume() {
		Log.i("RadioT", "resuming the activity");
		super.onResume();
		playbackController.start(LIVE_SHOW_URL);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.i("RadioT", "Getting activity config");
		Object[] result = new Object[] { playbackController };
		playbackController.detach();
		playbackController = null;
		return result;
	}

	@Override
	protected void onDestroy() {
		Log.i("RadioT", "Destroying activity");
		if (null != playbackController) {
			playbackController.stop();
		}
		unbindService(onService);
		super.onDestroy();
	}

	public void toggleLiveShow(View v) {
		playbackController.togglePlaying(playbackButton.isChecked());
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
