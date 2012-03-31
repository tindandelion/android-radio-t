package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.AudioStream;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();
    private static final String LIVE_SHOW_URL = "http://radio10.promodeejay.net:8181/stream";
    // private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";

    public static LiveShowApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(LiveShowApp app) {
        instance = app;
    }

    protected LiveShowApp() {
    }

    public AudioStream createAudioStream(MediaPlayer mediaPlayer) {
        return new AudioStream(mediaPlayer, LIVE_SHOW_URL);
    }
}
