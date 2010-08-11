package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.util.Log;

public class LiveShowPlaybackController implements
		MediaPlayer.OnPreparedListener {

	private MediaPlayer mediaPlayer;
	private String currentUrl;

	public LiveShowPlaybackController(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		mediaPlayer.setOnPreparedListener(this);
	}

	public void start(String url) {
		try {
			currentUrl = url;
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			Log.e("RadioT", "Playback exception", e);
		}
	}

	public void stop() {
		mediaPlayer.reset();
	}

	public void onPrepared(MediaPlayer mp) {
		mediaPlayer.start();
	}

	public void togglePlaying(boolean playing) {
		if (playing) {
			start(currentUrl);
		} else {
			stop();
		}
	}
}
