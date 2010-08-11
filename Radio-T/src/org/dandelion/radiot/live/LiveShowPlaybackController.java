package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.util.Log;

public class LiveShowPlaybackController {

	private MediaPlayer mediaPlayer;

	public LiveShowPlaybackController(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public void start(String url) {
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						public void onPrepared(MediaPlayer mp) {
							mp.start();
						}
					});
			mediaPlayer.prepareAsync();
			mediaPlayer.start();
		} catch (Exception e) {
			Log.e("RadioT", "Playback exception", e);
		}
	}

	public void stop() {
		mediaPlayer.reset();
	}
}
