package org.dandelion.radiot.live.service;

import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.states.PlaybackService;
import org.dandelion.radiot.live.states.PlaybackState;
import org.dandelion.radiot.live.states.Idle;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import org.dandelion.radiot.live.states.PlaybackStateVisitor;
import org.dandelion.radiot.live.ui.LiveShowActivity;

public class LiveShowService extends Service implements PlaybackService {
	public class LocalBinder extends Binder {
		public LiveShowService getService() {
			return (LiveShowService.this);
		}
	}

	public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
	protected static final String SCHEDULED_ACTION_TIMEOUT = "org.dandelion.radiot.live.ScheduledTimeoutExpired";
	private static final int NOTIFICATION_ID = 1;

	private final IBinder binder = new LocalBinder();
	private PlaybackState currentState;
	private String[] statusLabels;
	private Foregrounder foregrounder;
	private BroadcastReceiver onAlarm = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			currentState.onTimeout();
		}
	};
	private PendingIntent timeoutIntent;
	private AlarmManager alarmManager;
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
		currentState = new Idle(player, this);
		statusLabels = getResources().getStringArray(
				R.array.live_show_notification_labels);
		foregrounder = Foregrounder.create(this);

		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		timeoutIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
				new Intent(SCHEDULED_ACTION_TIMEOUT), 0);
		registerReceiver(onAlarm, new IntentFilter(SCHEDULED_ACTION_TIMEOUT));
		
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiLock = wm.createWifiLock("LiveShow");
		wifiLock.setReferenceCounted(false);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(onAlarm);
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

	public void acceptVisitor(PlaybackStateVisitor visitor) {
		currentState.acceptVisitor(visitor);
	}

	public PlaybackState getCurrentState() {
		return currentState;
	}

	public void stopPlayback() {
		currentState.stopPlayback();
	}

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

	public void runAsynchronously(Runnable runnable) {
		new Thread(runnable).start();
	}

	public void unscheduleTimeout() {
		alarmManager.cancel(timeoutIntent);
	}

	public void scheduleTimeout(int milliseconds) {
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ milliseconds, timeoutIntent);
	}

	public void lockWifi() {
		wifiLock.acquire();
	}

	public void unlockWifi() {
		wifiLock.release();
	}
}

