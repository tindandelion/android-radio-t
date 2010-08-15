package org.dandelion.radiot.live;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.LiveShowPlaybackController.ILivePlaybackView;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service {
	private final IBinder binder = new LocalBinder();
	private MediaPlayer mediaPlayer;
	private LiveShowPlaybackController playbackController;
	private String currentlyPlayingUrl;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = ((RadiotApplication)getApplication()).getMediaPlayer();
		playbackController = new LiveShowPlaybackController(mediaPlayer);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		if (!mediaPlayer.isPlaying()) {
			stopSelf();
		}
		return true;
	}
	
	@Override
	public void onDestroy() {
		playbackController.stop();
		super.onDestroy();
	}
	
	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

	public void startPlaying(String url) {
		if (playbackController.inProgress()) {
			return;
		}
		try {
			currentlyPlayingUrl = url;
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
			playbackController.isPreparing = true;
		} catch (Exception e) {
			showPlaybackError();
		}
		updateView();
	}

	protected void updateView() {
		if (null == playbackController.playbackView) {
			return;
		}
		playbackController.playbackView.enableControls(!playbackController.isPreparing);
		playbackController.playbackView.setPlaying(mediaPlayer.isPlaying());
	}

	protected void showPlaybackError() {
		if (null != playbackController.playbackView) {
			playbackController.playbackView.showPlaybackError();
		}
	}

	public void togglePlaying(boolean playing) {
		if (playing) {
			startPlaying(currentlyPlayingUrl);
		} else {
			stopPlaying();
		}
	}

	public void attach(ILivePlaybackView view) {
		playbackController.attach(view);
	}

	public void detach() {
		playbackController.detach();
	}

	public void stopPlaying() {
		playbackController.stop();
	}
}
