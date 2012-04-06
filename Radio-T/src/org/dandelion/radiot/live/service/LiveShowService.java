package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.*;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.util.IconNote;

public class LiveShowService extends Service implements LiveShowStateListener {
    public static final String TAG = LiveShowService.class.getName();
    public static final String TOGGLE_ACTION = TAG + ".Toggle";
    public static final String TIMEOUT_ACTION = "org.dandelion.radiot.live.Timeout";
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private WifiLocker wifiLocker;
    private NotificationController notificationController;
    private TimeoutScheduler scheduler;
    private AudioStream stream;

    @Override
	public void onCreate() {
		super.onCreate();
        wifiLocker = WifiLocker.create(this);
        notificationController = createNotificationController();
        scheduler = createWaitingScheduler();
        stream = createAudioStream();
        player = new LiveShowPlayer(stream, getStateHolder(), scheduler);
        scheduler.setPerformer(player);
        player.setListener(this);
    }

    private TimeoutScheduler createWaitingScheduler() {
        Timeout timeout = new AlarmTimeout(this, TIMEOUT_ACTION);
        return new TimeoutScheduler(timeout);
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
        return LiveShowApp.getInstance().createAudioStream();
    }

    private LiveShowStateHolder getStateHolder() {
        return LiveShowApp.getInstance().stateHolder();
    }

    @Override
    public void onDestroy() {
        player.setListener(null);
        wifiLocker.release();
        stream.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationController createNotificationController() {
        String[] labels = getResources().getStringArray(R.array.live_show_notification_labels);
        Foregrounder foregrounder = new Foregrounder(this);
        IconNote note = new LiveShowNote(getApplication(), NOTIFICATION_ID)
                .setIcon(R.drawable.stat_live)
                .setTitle(getString(R.string.app_name));
        return new NotificationController(foregrounder, labels, note);
    }

    @Override
    public void onStateChanged(LiveShowState newValue) {
        player.queryState(wifiLocker);
        player.queryState(notificationController);
    }
}
