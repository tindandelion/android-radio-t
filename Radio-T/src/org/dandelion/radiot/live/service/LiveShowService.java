package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.*;
import org.dandelion.radiot.util.IconNote;

public class LiveShowService extends Service implements LiveShowStateListener, PlayerActivityListener {
    public static final String TAG = LiveShowService.class.getName();
    public static final String TOGGLE_ACTION = TAG + ".Toggle";
    public static final String TIMEOUT_ACTION = "org.dandelion.radiot.live.Timeout";

    private LiveShowPlayer player;
    private WifiLocker wifiLocker;
    private TimeoutScheduler scheduler;
    private AudioStream stream;
    private LiveStatusDisplayer statusDisplayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        statusDisplayer = LiveShowApp.getInstance().createStatusDisplayer(this.getApplicationContext());
        wifiLocker = WifiLocker.create(this);
        scheduler = createWaitingScheduler();
        stream = createAudioStream();
        player = new LiveShowPlayer(stream, stateHolder(), scheduler);
        scheduler.setPerformer(player);
        player.setActivityListener(this);
        stateHolder().addListener(this);
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

    private LiveShowStateHolder stateHolder() {
        return LiveShowApp.getInstance().stateHolder();
    }

    @Override
    public void onDestroy() {
        stateHolder().removeListener(this);
        wifiLocker.release();
        stream.release();
        super.onDestroy();
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        statusDisplayer.showStatus(state);
        wifiLocker.updateLock(state);
    }

    @Override
    public void onActivated() {
        IconNote note = LiveShowApp.getInstance().createForegroundNote(this);
        startForeground(note.id(), note.build());
    }

    @Override
    public void onDeactivated() {
        stopSelf();
    }
}
