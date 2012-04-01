package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.*;
import org.dandelion.radiot.live.core.states.LiveShowState;

public class LiveShowService extends Service implements LiveShowStateListener {
    public static final String TAG = LiveShowService.class.getName();
    public static final String TOGGLE_ACTION = TAG + ".Toggle";
    public static final String TIMEOUT_ACTION = "org.dandelion.radiot.live.Timeout";
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private final IBinder binder = new LocalBinder();
    private WifiLocker wifiLocker;
    private NotificationController notificationController;
    private Timeout timeout;
    private MediaPlayer mediaPlayer;
    private TimeoutScheduler scheduler;

    public class LocalBinder extends Binder {
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
        timeout = new AlarmTimeout(this, TIMEOUT_ACTION);
        mediaPlayer = new MediaPlayer();
        scheduler = new TimeoutScheduler(timeout);
        player = new LiveShowPlayer(createAudioStream(), getStateHolder(), scheduler);
        scheduler.setPerformer(player);
        player.setListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (TOGGLE_ACTION.equals(action)) {
            player.togglePlayback();
        }
        if (TIMEOUT_ACTION.equals(action)) {
            scheduler.timeoutElapsed();
        }
        return START_STICKY;
    }

    private AudioStream createAudioStream() {
        return LiveShowApp.getInstance().createAudioStream(mediaPlayer);
    }

    private LiveShowStateHolder getStateHolder() {
        return LiveShowApp.getInstance().stateHolder();
    }

    @Override
    public void onDestroy() {
        player.setListener(null);
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
    public void onStateChanged(LiveShowState newValue) {
        player.queryState(wifiLocker);
        player.queryState(notificationController);
    }
}
