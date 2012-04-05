package org.dandelion.radiot.live;

import android.content.Context;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.core.LiveShowStateListener;
import org.dandelion.radiot.live.core.states.Idle;
import org.dandelion.radiot.live.service.LiveShowClient;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();
    private static final String LIVE_SHOW_URL = "http://radio10.promodeejay.net:8181/stream";
    private LiveShowStateHolder stateHolder = new LiveShowStateHolder(new Idle());
    // private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";

    public static LiveShowApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(LiveShowApp app) {
        instance = app;
    }

    protected LiveShowApp() {
    }

    public AudioStream createAudioStream() {
        return new AudioStream(LIVE_SHOW_URL);
    }
    
    public LiveShowClient createClient(Context context, LiveShowStateListener listener) {
        return new LiveShowClient(context, stateHolder, listener);
    }

    public LiveShowStateHolder stateHolder() {
        return stateHolder;
    }
}
