package org.dandelion.radiot.live;

import android.app.Service;
import android.content.Context;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.service.Lockable;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();
    private static final String LIVE_SHOW_URL = "http://stream.radio-t.com/stream";
    // private static final String LIVE_SHOW_URL = "http://172.20.10.5:4567/stream";
    private LiveShowStateHolder stateHolder = LiveShowStateHolder.initial();

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

    public LiveShowStateListener createStatusDisplayer(Service service) {
        return new LiveShowStatusDisplayer(service);
    }

    public Lockable createNetworkLock(Context context) {
        return NetworkLock.create(context);
    }
}
