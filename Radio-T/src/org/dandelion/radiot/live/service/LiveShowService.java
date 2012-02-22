package org.dandelion.radiot.live.service;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.live.core.LiveShowState.ILiveShowService;
import org.dandelion.radiot.live.core.LiveShowState.Idle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;

public class LiveShowService extends Service implements ILiveShowService {

    public class LocalBinder extends Binder {
		public LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
    private static final int NOTIFICATION_ID = 1;

	private final IBinder binder = new LocalBinder();
	private LiveShowState currentState;
	private String[] statusLabels;
	private Foregrounder foregrounder;
    private Runnable onAlarm = new Runnable() {
        @Override
        public void run() {
            currentState.onTimeout();
        }
    };
    private Alarm alarm;
	private WifiLock wifiLock;

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
		statusLabels = getResources().getStringArray(
				R.array.live_show_notification_labels);
		foregrounder = Foregrounder.create(this);

        alarm = new Alarm(this);
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiLock = wm.createWifiLock("LiveShow");
		wifiLock.setReferenceCounted(false);
	}

    @Override
	public void onDestroy() {
		alarm.release();
		wifiLock.release();
		super.onDestroy();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		if (currentState instanceof Idle) { 
			stopSelf();
		}
		return true;
	}

	public void acceptVisitor(LiveShowState.ILiveShowVisitor visitor) {
		currentState.acceptVisitor(visitor);
	}

    public LiveShowState getCurrentState() {
		return currentState;
	}

	public void stopPlayback() {
		currentState.stopPlayback();
	}

	public synchronized void switchToNewState(LiveShowState newState) {
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

	public void runAsynchronously(Runnable runnable) {
		new Thread(runnable).start();
	}

	public void unscheduleTimeout() {
        alarm.reset();
    }

    public void scheduleTimeout(int milliseconds) {
        alarm.set(milliseconds, onAlarm);
    }

    public void lockWifi() {
		wifiLock.acquire();
	}

	public void unlockWifi() {
		wifiLock.release();
	}
}

