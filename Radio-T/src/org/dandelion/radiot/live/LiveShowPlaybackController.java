package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.util.Log;

public class LiveShowPlaybackController implements
		MediaPlayer.OnPreparedListener {
	
	public interface ILivePlaybackView {
		void enableControls(boolean enabled);
		void setPlaying(boolean playing);
	}

	private MediaPlayer mediaPlayer;
	private String currentUrl;
	private ILivePlaybackView playbackView;

	public LiveShowPlaybackController(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		playbackView = new NullPlaybackView();
		mediaPlayer.setOnPreparedListener(this);
	}
	
	public void attach(ILivePlaybackView view) { 
		playbackView = view;
	}

	public void start(String url) {
		try {
			currentUrl = url;
			playbackView.setPlaying(false);
			playbackView.enableControls(false);
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			Log.e("RadioT", "Playback exception", e);
		}
	}

	public void stop() {
		mediaPlayer.reset();
		playbackView.setPlaying(false);
	}

	public void onPrepared(MediaPlayer mp) {
		mediaPlayer.start();
		playbackView.enableControls(true);
		playbackView.setPlaying(true);
	}

	public void togglePlaying(boolean playing) {
		if (playing) {
			start(currentUrl);
		} else {
			stop();
		}
	}
}

class NullPlaybackView implements LiveShowPlaybackController.ILivePlaybackView {

	public void enableControls(boolean enabled) {
	}

	public void setPlaying(boolean playing) {
		// TODO Auto-generated method stub
		
	}
}
