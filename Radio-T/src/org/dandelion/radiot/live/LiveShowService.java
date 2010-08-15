package org.dandelion.radiot.live;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.LiveShowPlaybackController.ILivePlaybackView;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LiveShowService extends Service {
	private final IBinder binder = new LocalBinder();
	private MediaPlayer mediaPlayer;
	private LiveShowPlaybackController playbackController;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("RadioT", "Binding to service");
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = ((RadiotApplication)getApplication()).getMediaPlayer();
		playbackController = new LiveShowPlaybackController(mediaPlayer);
		Log.i("RadioT", "Playback service created");
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i("RadioT", "Unbinding from service");
		if (!mediaPlayer.isPlaying()) {
			stopSelf();
		}
		return true;
	}
	
	@Override
	public void onDestroy() {
		Log.i("RadioT", "Playback service destroyed");
		playbackController.stop();
		super.onDestroy();
	}
	
	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

	public void start(String url) {
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
}
