package org.dandelion.radiot.live;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service {
	private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
	
	private final IBinder binder = new LocalBinder();
	private MediaPlayer player = new MediaPlayer();
	private OnPreparedListener onPrepared = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			mp.start();
			sendPlaybackStateChangedBroadcast();
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
		if (player.isPlaying())
			return;

		try {
			player.setDataSource(LIVE_SHOW_URL);
			player.setOnPreparedListener(onPrepared);
			player.prepareAsync();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public boolean isPlaying() {
		return player.isPlaying();
	}


	public void stopPlayback() {
		player.reset();
		sendPlaybackStateChangedBroadcast();
	}

	private void sendPlaybackStateChangedBroadcast() {
		sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}
}
