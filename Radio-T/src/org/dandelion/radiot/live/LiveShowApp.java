package org.dandelion.radiot.live;

import android.app.PendingIntent;
import android.content.Context;
import org.dandelion.radiot.R;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.service.LiveShowService;
import org.dandelion.radiot.live.service.Lockable;
import org.dandelion.radiot.live.ui.LiveNotificationManager;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.util.IconNote;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();
    private static final String LIVE_SHOW_URL = "http://stream.radio-t.com/stream";
    // private static final String LIVE_SHOW_URL = "http://10.0.1.2:4567/stream";
    private LiveShowStateHolder stateHolder = LiveShowStateHolder.initial();
    private static final int FOREGROUND_NOTE_ID = 1;
    private static final int BACKGROUND_NOTE_ID = 2;

    public static LiveShowApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(LiveShowApp app) {
        instance = app;
    }

    public LiveShowApp() {
    }


    public AudioStream createAudioStream() {
        return new MediaPlayerStream(LIVE_SHOW_URL);
    }

    public LiveShowClient createClient(Context context) {
        return new LiveShowClient(context, stateHolder);
    }

    public LiveShowStateHolder stateHolder() {
        return stateHolder;
    }

    public LiveNotificationManager createNotificationManager(Context context) {
        IconNote foregroundNote = createForegroundNotification(context);
        IconNote backgroundNote = createBackgroundNotification(context);
        return new LiveNotificationManager(foregroundNote, backgroundNote);
    }

    public IconNote createForegroundNotification(final Context context) {
        return createNotification(context, FOREGROUND_NOTE_ID);
    }

    public IconNote createBackgroundNotification(final Context context) {
        return createNotification(context, BACKGROUND_NOTE_ID);
    }

    private IconNote createNotification(Context context, int id) {
        String actionTitle = context.getResources().getStringArray(R.array.live_show_button_labels)[0];
        PendingIntent actionIntent = PendingIntent.getService(
                context, 0, LiveShowService.createToggleIntent(context), 0);
        return new IconNote(context.getApplicationContext(), id)
                .setTitle(context.getString(R.string.app_name))
                .setIcon(R.drawable.stat_live)
                .showsActivity(LiveShowActivity.class)
                .addAction(R.drawable.ic_stop_notification, actionTitle, actionIntent)
                .beOngoing();
    }

    public Lockable createNetworkLock(Context context) {
        return NetworkLock.create(context);
    }
}
