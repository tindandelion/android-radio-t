package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.util.Log;

public class LiveShowPlaybackController {

	public interface ILivePlaybackView {
		void enableControls(boolean enabled);

		void setPlaying(boolean playing);

		void showPlaybackError();
	}

	private MediaPlayer mediaPlayer;
	ILivePlaybackView playbackView;
	boolean isPreparing = false;

	public LiveShowPlaybackController(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
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
	}

	protected void showPlaybackError() {
		if (null != playbackView) {
			playbackView.showPlaybackError();
		}
	}

	boolean inProgress() {
		return isPreparing || mediaPlayer.isPlaying();
	}

	public void stop() {
		mediaPlayer.reset();
		updateView();
	}

	public void detach() {
		playbackView = null;
	}
}
