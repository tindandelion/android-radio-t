package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotActivity;
import org.dandelion.radiot.RadiotApplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LiveShowActivity extends RadiotActivity implements
		LiveShowPlaybackController.ILivePlaybackView {
	// public static final Uri LIVE_SHOW_URL = Uri
	// .parse("http://stream3.radio-t.com:8181/stream");

	public static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";

	LiveShowPlaybackController playbackController;

	private ToggleButton playbackButton;

	private Object[] savedState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		playbackButton = (ToggleButton) findViewById(R.id.live_show_playback_button);
		createPlaybackController();
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
		super.onResume();
		playbackController.start(LIVE_SHOW_URL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != playbackController) {
			playbackController.stop();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		savedState = new Object[] { playbackController };
		playbackController.detach();
		playbackController = null;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return savedState;
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
