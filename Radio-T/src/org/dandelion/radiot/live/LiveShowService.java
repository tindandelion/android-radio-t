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
		playbackController.start(url);
	}

	public void togglePlaying(boolean playing) {
		playbackController.togglePlaying(playing);
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
