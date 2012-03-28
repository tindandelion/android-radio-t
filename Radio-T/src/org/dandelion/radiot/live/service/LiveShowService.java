package org.dandelion.radiot.live.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.LiveShowState;

public class LiveShowService extends Service implements LiveShowPlayer.StateChangeListener {
    private static final int NOTIFICATION_ID = 1;

    private LiveShowPlayer player;
    private final IBinder binder = new LocalBinder();
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
        wifiLocker = WifiLocker.create(this);
        notificationController = createNotificationController();
        player = LiveShowApp.getInstance().getLiveShowPlayer();
        player.setListener(this);
    }

    @Override
    public void onDestroy() {
        wifiLocker.release();
        player.setListener(null);
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

    public void reset() {
        player.reset();
	}

    public void togglePlayback() {
        player.togglePlayback();
    }
}
