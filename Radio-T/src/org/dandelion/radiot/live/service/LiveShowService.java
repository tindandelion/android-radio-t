package org.dandelion.radiot.live.service;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.Idle;
import org.dandelion.radiot.live.core.states.PlaybackState;
import org.dandelion.radiot.live.core.Timeout;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.live.core.states.PlaybackState.ILiveShowService;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service implements ILiveShowService {

    public class LocalBinder extends Binder {
		public LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
    private static final String TIMEOUT_ELAPSED = "org.dandelion.radiot.live.TimeoutElapsed";
    private static final int NOTIFICATION_ID = 1;

	private final IBinder binder = new LocalBinder();
	private PlaybackState currentState;
	private String[] statusLabels;
	private Foregrounder foregrounder;
    private Timeout waitTimeout;
    private NetworkLock networkLock;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MediaPlayer player = ((RadiotApplication) getApplication())
				.getMediaPlayer();
        AudioStream liveStream = new AudioStream(player);
		currentState = new Idle(new PlaybackContext(this, liveStream));
		statusLabels = getResources().getStringArray(
				R.array.live_show_notification_labels);
		foregrounder = Foregrounder.create(this);
        waitTimeout = new AlarmTimeout(this, TIMEOUT_ELAPSED);
        networkLock = new NetworkLock(this);
    }

    @Override
	public void onDestroy() {
        waitTimeout.release();
		networkLock.release();
		super.onDestroy();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		if (currentState instanceof Idle) {
			stopSelf();
		}
		return true;
	}

	public void acceptVisitor(LiveShowQuery visitor) {
		currentState.acceptVisitor(visitor);
	}

    public PlaybackState getCurrentState() {
		return currentState;
	}

	public void stopPlayback() {
		currentState.stopPlayback();
	}

    // TODO: Get rid of saving current state
	public synchronized void switchToNewState(PlaybackState newState) {
		currentState.leave();
		newState.enter();
		currentState = newState;
		sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
	}

	public void goForeground(int statusLabelIndex) {
		foregrounder.startForeground(NOTIFICATION_ID,
				createNotification(statusLabels[statusLabelIndex]));
	}

	public void goBackground() {
		foregrounder.stopForeground();
	}

	private Notification createNotification(String statusMessage) {
		Notification note = new Notification(R.drawable.ic_notification_live,
				null, System.currentTimeMillis());
		PendingIntent i = PendingIntent.getActivity(getApplication(), 0,
				new Intent(getApplication(), LiveShowActivity.class), 0);
		note.setLatestEventInfo(getApplication(), getString(R.string.app_name),
				statusMessage, i);
		return note;
	}

    public void resetTimeout() {
        waitTimeout.reset();
    }

    public void setTimeout(int milliseconds, Runnable action) {
        waitTimeout.set(milliseconds, action);
    }

    public void lockWifi() {
		networkLock.acquire();
	}

	public void unlockWifi() {
		networkLock.release();
	}
}

