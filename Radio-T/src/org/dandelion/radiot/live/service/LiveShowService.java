package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.*;
import org.dandelion.radiot.util.IconNote;

public class LiveShowService extends Service implements PlayerActivityListener {
    public static final String TAG = LiveShowService.class.getName();
    public static final String TOGGLE_ACTION = TAG + ".Toggle";
    public static final String TIMEOUT_ACTION = "org.dandelion.radiot.live.Timeout";

    private LiveShowPlayer player;
    private TimeoutScheduler scheduler;
    private AudioStream stream;
    private LiveShowStateListener statusDisplayer;
    private WifiManager.WifiLock wifiLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createInfrastructure();
        createPlayer();
        createVisual();
    }

    private void createInfrastructure() {
        wifiLock = app().createWifiLock(this);
        scheduler = new TimeoutScheduler(new AlarmTimeout(this, TIMEOUT_ACTION));
        stream = app().createAudioStream();
    }

    private void createPlayer() {
        player = new LiveShowPlayer(stream, stateHolder(), scheduler);
        scheduler.setPerformer(player);
        player.setActivityListener(this);
    }

    private void createVisual() {
        statusDisplayer = app().createStatusDisplayer(this.getApplicationContext());
        stateHolder().addListener(statusDisplayer);
    }

    @Override
    public void onDestroy() {
        releaseVisual();
        releaseInfrastructure();
        super.onDestroy();
    }

    private void releaseVisual() {
        stateHolder().removeListener(statusDisplayer);
    }

    private void releaseInfrastructure() {
        wifiLock.release();
        stream.release();
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
        return START_NOT_STICKY;
    }

    private LiveShowApp app() {
        return LiveShowApp.getInstance();
    }

    private LiveShowStateHolder stateHolder() {
        return app().stateHolder();
    }

    @Override
    public void onActivated() {
        IconNote note = app().createForegroundNote(this);
        startForeground(note.id(), note.build());
        wifiLock.acquire();
    }

    @Override
    public void onDeactivated() {
        stopSelf();
    }
}
