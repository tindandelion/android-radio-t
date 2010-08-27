package org.dandelion.radiot.live;

import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.LiveShowState.StateNames;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service {
	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";

	private final IBinder binder = new LocalBinder();
	private LiveShowState currentState;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MediaPlayer player = ((RadiotApplication) getApplication())
				.getMediaPlayer();
		currentState = new LiveShowState.Idle(player, this);
	}

	public void startPlayback() {
		currentState.startPlayback();
	}

	public StateNames getState() {
		return currentState.getName();
	}

	public void stopPlayback() {
		currentState.stopPlayback();
	}

	void switchToNewState(LiveShowState newState) {
		newState.enter();
		currentState = newState;
		sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}
}
