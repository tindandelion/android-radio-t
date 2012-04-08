package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.*;
import org.dandelion.radiot.live.core.states.Idle;
import org.dandelion.radiot.live.core.states.LiveShowState;
import org.dandelion.radiot.util.IconNote;

public class LiveShowService extends Service implements LiveShowStateListener {
    public static final String TAG = LiveShowService.class.getName();
    public static final String TOGGLE_ACTION = TAG + ".Toggle";
    public static final String TIMEOUT_ACTION = "org.dandelion.radiot.live.Timeout";
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private WifiLocker wifiLocker;
    private ForegroundController foregroundController;
    private TimeoutScheduler scheduler;
    private AudioStream stream;
    private LiveStatusDisplayer statusDisplayer;

    @Override
    public void onCreate() {
        super.onCreate();
        statusDisplayer = new LiveStatusDisplayer(
                LiveShowApp.getInstance().createNotificatioBar(this),
                getString(R.string.app_name),
                getResources().getStringArray(R.array.live_show_notification_labels));
        wifiLocker = WifiLocker.create(this);
        foregroundController = createNotificationController();
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

    private ForegroundController createNotificationController() {
        Foregrounder foregrounder = new Foregrounder(this);
        IconNote note = new LiveShowNote(getApplication(), NOTIFICATION_ID)
                .setIcon(R.drawable.stat_live)
                .setTitle(getString(R.string.app_name));
        return new ForegroundController(foregrounder, note);
    }

    @Override
    public void onStateChanged(LiveShowState state) {
        statusDisplayer.updateStatus(state);
        state.acceptVisitor(wifiLocker);
    }

    private static class LiveStatusDisplayer {
        private final NotificationBar notificationBar;
        private final String title;
        private final String[] statusLabels;

        private LiveStatusDisplayer(NotificationBar notificationBar, String title, String[] statusLabels) {
            this.notificationBar = notificationBar;
            this.title = title;
            this.statusLabels = statusLabels;
        }

        public void updateStatus(LiveShowState state) {
            if (state.getClass() == Idle.class) {
                notificationBar.hideIcon(LiveShowApp.LIVE_NOTIFICATION_ID);
            } else {
                final String iconTitle = title;
                final String iconText = statusLabels[0];
                notificationBar.showIcon(
                        LiveShowApp.LIVE_NOTIFICATION_ID,
                        LiveShowApp.LIVE_ICON_RESOURCE_ID,
                        iconTitle,
                        iconText);
            }
        }
    }
}
