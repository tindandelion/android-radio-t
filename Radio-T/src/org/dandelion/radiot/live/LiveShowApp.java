package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.AudioStream;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();

    public static LiveShowApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(LiveShowApp app) {
        instance = app;
    }

    protected LiveShowApp() {
    }

    public AudioStream createAudioStream(MediaPlayer mediaPlayer) {
        return new AudioStream(mediaPlayer);
    }
}
