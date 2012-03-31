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
    public static final String TIMEOUT_ACTION = "org.dandelion.radiot.live.TimeoutElapsed";
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private final IBinder binder = new LocalBinder();
    private WifiLocker wifiLocker;
    private NotificationController notificationController;
    private Timeout waitTimeout;
    private MediaPlayer mediaPlayer;

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
        wifiLocker = WifiLocker.create(this);
        notificationController = createNotificationController();
        waitTimeout = new AlarmTimeout(this, TIMEOUT_ACTION);
        mediaPlayer = new MediaPlayer();
        player = new LiveShowPlayer(createAudioStream(), waitTimeout);
        player.setListener(this);
    }

    private AudioStream createAudioStream() {
        return LiveShowApp.getInstance().createAudioStream(mediaPlayer);
    }

    @Override
    public void onDestroy() {
        player.setListener(null);
        waitTimeout.release();
        wifiLocker.release();
        mediaPlayer.release();
        super.onDestroy();
    }

    private NotificationController createNotificationController() {
        String[] labels = getResources().getStringArray(R.array.live_show_notification_labels);
        Foregrounder foregrounder = new Foregrounder(this, NOTIFICATION_ID);
        NotificationBuilder nb = new NotificationBuilder(getApplication(), R.drawable.stat_live,
                getString(R.string.app_name));
        return new NotificationController(foregrounder, nb, labels);
    }


    @Override
    public void onChangedState(LiveShowState newState) {
        player.queryState(wifiLocker);
        player.queryState(notificationController);
        PlaybackStateChangedEvent.send(this, newState);
    }

    // TODO: Stupid delegation to player, just return the player?
	public void queryState(LiveShowPlayer.StateVisitor visitor) {
        player.queryState(visitor);
	}

    public void togglePlayback() {
        player.togglePlayback();
    }
}
