package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import org.dandelion.radiot.R;
import org.dandelion.radiot.RadiotApplication;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.Timeout;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.live.core.states.LiveShowState.ILiveShowService;

public class LiveShowService extends Service implements ILiveShowService, LiveShowPlayer.StateChangeListener {

    public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
    private static final String TIMEOUT_ELAPSED = "org.dandelion.radiot.live.TimeoutElapsed";
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private final IBinder binder = new LocalBinder();
    private String[] statusLabels;
    private Foregrounder foregrounder;
    private Timeout waitTimeout;
    private WifiLocker wifiLocker;
    private NotificationBuilder notificationBuilder;
    private NotificationController notificationController;

    public class LocalBinder extends Binder {

        public LiveShowService getService() {
			return (LiveShowService.this);
		}
    }

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

    @Override
    public boolean onUnbind(Intent intent) {
        if (player.isIdle()) {
            stopSelf();
        }
        return true;
    }

	@Override
	public void onCreate() {
		super.onCreate();
		MediaPlayer player = ((RadiotApplication) getApplication())
				.getMediaPlayer();
        AudioStream liveStream = new AudioStream(player);
        waitTimeout = new AlarmTimeout(this, TIMEOUT_ELAPSED);

        this.player = new LiveShowPlayer(liveStream, waitTimeout);
        this.player.setListener(this);

		statusLabels = getResources().getStringArray(
				R.array.live_show_notification_labels);
		foregrounder = Foregrounder.create(this);
        wifiLocker = WifiLocker.create(this);
        notificationBuilder = new NotificationBuilder(getApplication(), R.drawable.ic_notification_live,
                getString(R.string.app_name));
        notificationController = new NotificationController(NOTIFICATION_ID, foregrounder, notificationBuilder, statusLabels);
    }

    @Override
	public void onDestroy() {
        waitTimeout.release();
		wifiLocker.release();
		super.onDestroy();
	}

    @Override
    public void onChangedState(LiveShowState oldState, LiveShowState newState) {
        player.queryState(wifiLocker);
        player.queryState(notificationController);
        oldState.leave(this);
        newState.enter(this);
        sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
    }

	public void acceptVisitor(LiveShowPlayer.StateVisitor visitor) {
        player.queryState(visitor);
	}

    public LiveShowState getCurrentState() {
        return player.getState();
	}

	public void stopPlayback() {
        player.stopPlayback();
	}

    public void goForeground(int statusLabelIndex) {
        foregrounder.startForeground(NOTIFICATION_ID,
                notificationBuilder.createNotification(statusLabels[statusLabelIndex]));
	}

	public void goBackground() {
		foregrounder.stopForeground();
	}

}

