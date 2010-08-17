package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service {
	private final IBinder binder = new LocalBinder();
	private String currentlyPlayingUrl;
	private MediaPlayer mediaPlayer;
	protected boolean isPreparing = false;

	private OnErrorListener onError = new OnErrorListener() {
		public boolean onError(MediaPlayer mp, int what, int extra) {
			showPlaybackError();
			isPreparing = false;
			stopPlaying();
			return true;
		}
	};
	private OnPreparedListener onPrepared = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			isPreparing = false;
			mediaPlayer.start();
			startForeground(1, createNotification());
			updateView();
		}
	};

	private ILivePlaybackView playbackView;

	public void attach(ILivePlaybackView view) {
		playbackView = view;
		updateView();
	}

	private Notification createNotification() {
		Notification note = new Notification(R.drawable.status_icon, null,
				System.currentTimeMillis());
		PendingIntent i = PendingIntent.getActivity(getApplication(), 0,
				new Intent(getApplication(), LiveShowActivity.class), 0);
		note.setLatestEventInfo(getApplication(),
				getString(R.string.app_name),
				getString(R.string.live_show_status_string), i);
		return note;
	}

	public void detach() {
		playbackView = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = ((RadiotApplication) getApplication()).getMediaPlayer();
		mediaPlayer.setOnPreparedListener(onPrepared);
		mediaPlayer.setOnErrorListener(onError);
	}

	@Override
	public void onDestroy() {
		stopPlaying();
		mediaPlayer.setOnPreparedListener(null);
		mediaPlayer.setOnErrorListener(null);
		super.onDestroy();
	}

	public void startPlaying(String url) {
		if (isIdle()) {
			try {
				currentlyPlayingUrl = url;
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepareAsync();
				isPreparing = true;
			} catch (Exception e) {
				showPlaybackError();
			}
			updateView();
		}
	}

	public void stopPlaying() {
		mediaPlayer.reset();
		stopForeground(true);
		updateView();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		if (!mediaPlayer.isPlaying()) { 
			stopSelf();
		}
		return true;
	}

	public void togglePlaying(boolean playing) {
		if (playing) {
			startPlaying(currentlyPlayingUrl);
		} else {
			stopPlaying();
		}
	}

	private boolean isIdle() {
		return !(isPreparing || mediaPlayer.isPlaying());
	}

	private void showPlaybackError() {
		if (null != playbackView) {
			playbackView.showPlaybackError();
		}
	}

	private void updateView() {
		if (null != playbackView) {
			playbackView.enableControls(!isPreparing);
			playbackView.setPlaying(mediaPlayer.isPlaying());
		}
	}

	public interface ILivePlaybackView {
		void enableControls(boolean enabled);

		void setPlaying(boolean playing);

		void showPlaybackError();
	}

	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}
}
