package org.dandelion.radiot.live;

import org.dandelion.radiot.RadiotApplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service {
	private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
	private final IBinder binder = new LocalBinder();
	private MediaPlayer player;
	private OnPreparedListener onPrepared = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			mp.start();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		RadiotApplication application = (RadiotApplication) getApplication();
		player = application.getMediaPlayer();
	}

	public void startPlayback() {
		try {
			player.setDataSource(LIVE_SHOW_URL);
			player.setOnPreparedListener(onPrepared);
			player.prepareAsync();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

}
