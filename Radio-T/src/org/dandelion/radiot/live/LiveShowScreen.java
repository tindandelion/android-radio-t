package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotActivity;
import org.dandelion.radiot.RadiotApplication;

import android.os.Bundle;

public class LiveShowScreen extends RadiotActivity {
	// public static final Uri LIVE_SHOW_URL = Uri
	// .parse("http://stream3.radio-t.com:8181/stream");

	public static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";

	LiveShowPlaybackController playbackController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
		playbackController = new LiveShowPlaybackController(
				((RadiotApplication) getApplication()).getMediaPlayer());
	}

	@Override
	protected void onResume() {
		super.onResume();
		playbackController.start(LIVE_SHOW_URL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		playbackController.stop();
	}
}
