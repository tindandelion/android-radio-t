package org.dandelion.radiot.live.service;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.*;
import org.dandelion.radiot.util.IconNote;

public class LiveShowService extends WakefulService implements PlayerActivityListener {
    private static final String TAG = LiveShowService.class.getName();
    private static final String TOGGLE_ACTION = TAG + ".Toggle";
    private static final String TIMEOUT_ACTION = TAG + ".Timeout";

    private LiveShowPlayer player;
    private TimeoutScheduler scheduler;
    private AudioStream stream;
    private LiveShowStateListener statusDisplayer;
    private Lockable networkLock;

    public static void sendTimeoutElapsed(Context context) {
        performWakefulAction(context, LiveShowService.class, TIMEOUT_ACTION);
    }

    public static void sendTogglePlayback(Context context) {
        performWakefulAction(context, LiveShowService.class, TOGGLE_ACTION);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createInfrastructure();
        createPlayer();
        createVisual();
    }

    private void createInfrastructure() {
        networkLock = app().createNetworkLock(this);
        scheduler = new TimeoutScheduler(new AlarmTimeout(this, TimeoutReceiver.BROADCAST));
        stream = app().createAudioStream();
    }

    private void createPlayer() {
        player = new LiveShowPlayer(stream, stateHolder(), scheduler);
        scheduler.setPerformer(player);
        player.setActivityListener(this);
    }

    private void createVisual() {
        statusDisplayer = app().createStatusDisplayer(this.getApplicationContext());
        stateHolder().addListenerSilently(statusDisplayer);
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
        networkLock.release();
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
        networkLock.acquire();
    }

    @Override
    public void onDeactivated() {
        stopSelf();
    }
}
