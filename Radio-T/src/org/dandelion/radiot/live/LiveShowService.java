package org.dandelion.radiot.live;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service {

	public enum PlaybackState {
		Waiting, Playing, Idle
	}

	private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";

	private final IBinder binder = new LocalBinder();
	private PlaybackState currentState = PlaybackState.Idle;
	private MediaPlayer player = new MediaPlayer();
	private OnPreparedListener onPrepared = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			switchToPlayingState();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void setMediaPlayer(MediaPlayer player) {
		this.player = player;
	}

	public void startPlayback() {
		if (currentState != PlaybackState.Idle)
			return;
		switchToWaitingState();
	}

	public boolean isPlaying() {
		return currentState == PlaybackState.Playing;
	}

	public PlaybackState getState() {
		return currentState;
	}

	public void stopPlayback() {
		switchToIdleState();
	}

	private void switchToIdleState() {
		player.reset();
		changeCurrentState(PlaybackState.Idle);
	}

	private void switchToWaitingState() {
		try {
			player.setDataSource(LIVE_SHOW_URL);
			player.setOnPreparedListener(onPrepared);
			player.prepareAsync();
			changeCurrentState(PlaybackState.Waiting);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void switchToPlayingState() {
		player.start();
		changeCurrentState(PlaybackState.Playing);
	}

	private void changeCurrentState(PlaybackState newState) {
		currentState = newState;
		sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}
}
