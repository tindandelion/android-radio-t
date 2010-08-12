package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.util.Log;

public class LiveShowPlaybackController implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

	public interface ILivePlaybackView {
		void enableControls(boolean enabled);

		void setPlaying(boolean playing);

		void showPlaybackError();
	}

	private MediaPlayer mediaPlayer;
	private String currentUrl;
	private ILivePlaybackView playbackView;
	private boolean isPreparing = false;

	public LiveShowPlaybackController(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	public void attach(ILivePlaybackView view) {
		playbackView = view;
		updateView();
	}

	private void updateView() {
		if (null == playbackView) {
			return;
		}
		playbackView.enableControls(!isPreparing);
		playbackView.setPlaying(mediaPlayer.isPlaying());
	}

	public void start(String url) {
		if (inProgress()) {
			return;
		}
		try {
			currentUrl = url;
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
			isPreparing = true;
		} catch (Exception e) {
			showPlaybackError();
			Log.e("RadioT", "Live error", e);
		}
		updateView();
	}

	protected void showPlaybackError() {
		if (null != playbackView) {
			playbackView.showPlaybackError();
		}
	}

	private boolean inProgress() {
		return isPreparing || mediaPlayer.isPlaying();
	}

	public void stop() {
		mediaPlayer.reset();
		updateView();
	}

	public void onPrepared(MediaPlayer mp) {
		isPreparing = false;
		mediaPlayer.start();
		updateView();
	}

	public void togglePlaying(boolean playing) {
		if (playing) {
			start(currentUrl);
		} else {
			stop();
		}
	}

	public void detach() {
		playbackView = null;
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		showPlaybackError();
		
		isPreparing = false;
		stop();
		return true;
	}
}
