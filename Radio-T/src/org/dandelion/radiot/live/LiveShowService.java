package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.LiveShowState.ILiveShowService;
import org.dandelion.radiot.live.LiveShowState.StateNames;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service implements ILiveShowService {
	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
	private static final int NOTIFICATION_ID = 1;

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

	public void switchToNewState(LiveShowState newState) {
		newState.enter();
		currentState = newState;
		sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public class LocalBinder extends Binder {
		LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

	public void goForeground(int stringId) {
		startForeground(NOTIFICATION_ID,
				createNotification(getString(stringId)));
	}

	public void goBackground() {
		stopForeground(true);
	}

	private Notification createNotification(String statusMessage) {
		Notification note = new Notification(R.drawable.status_icon, null,
				System.currentTimeMillis());
		PendingIntent i = PendingIntent.getActivity(getApplication(), 0,
				new Intent(getApplication(), LiveShowActivity.class), 0);
		note.setLatestEventInfo(getApplication(), getString(R.string.app_name),
				statusMessage, i);
		return note;
	}

}
