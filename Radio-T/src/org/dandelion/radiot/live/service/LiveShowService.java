package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.Timeout;
import org.dandelion.radiot.live.core.states.LiveShowState;

public class LiveShowService extends Service implements LiveShowPlayer.StateChangeListener {
    public static final String PLAYBACK_STATE_CHANGED = "org.dandelion.radiot.live.PlaybackStateChanged";
    private static final String TIMEOUT_ELAPSED = "org.dandelion.radiot.live.TimeoutElapsed";
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private final IBinder binder = new LocalBinder();
    private Timeout waitTimeout;
    private WifiLocker wifiLocker;
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
        waitTimeout = new AlarmTimeout(this, TIMEOUT_ELAPSED);
        wifiLocker = WifiLocker.create(this);
        notificationController = createNotificationController();
        player = createPlayer(waitTimeout);
    }

    @Override
    public void onDestroy() {
        waitTimeout.release();
        wifiLocker.release();
        super.onDestroy();
    }

    private LiveShowPlayer createPlayer(Timeout timeout) {
        MediaPlayer player = LiveShowApp.getInstance().getMediaPlayer();
        AudioStream liveStream = new AudioStream(player);
        LiveShowPlayer p = new LiveShowPlayer(liveStream, timeout);
        p.setListener(this);
        return p;
    }

    private NotificationController createNotificationController() {
        String[] labels = getResources().getStringArray(R.array.live_show_notification_labels);
        Foregrounder foregrounder = Foregrounder.create(this, NOTIFICATION_ID);
        NotificationBuilder nb = new NotificationBuilder(getApplication(), R.drawable.ic_notification_live,
                getString(R.string.app_name));
        return new NotificationController(foregrounder, nb, labels);
    }


    @Override
    public void onChangedState(LiveShowState newState) {
        player.queryState(wifiLocker);
        player.queryState(notificationController);
        sendBroadcast(new Intent(LiveShowService.PLAYBACK_STATE_CHANGED));
    }

    // TODO: Stupid delegation to player, just return the player?
	public void queryState(LiveShowPlayer.StateVisitor visitor) {
        player.queryState(visitor);
	}

    public void stopPlayback() {
        player.stopPlayback();
	}

    public void startPlayback() {
        player.startPlayback();
    }
}
