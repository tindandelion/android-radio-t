package org.dandelion.radiot.live;

import android.content.Context;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.service.LiveShowClient;
import org.dandelion.radiot.live.ui.LiveStatusDisplayer;
import org.dandelion.radiot.live.ui.NotificationStatusDisplayer;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();
    //private static final String LIVE_SHOW_URL = "http://radio10.promodeejay.net:8181/stream";
    private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
    private LiveShowStateHolder stateHolder = LiveShowStateHolder.initial();
    private static final int LIVE_NOTE_ID = 1;

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

    public LiveStatusDisplayer createStatusDisplayer(Context context) {
        return new NotificationStatusDisplayer(context, LIVE_NOTE_ID);
    }
}
