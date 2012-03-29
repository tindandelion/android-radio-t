package org.dandelion.radiot.live;

import android.content.Context;
import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.service.AlarmTimeout;

public class LiveShowApp {
    private static final String TIMEOUT_ELAPSED = "org.dandelion.radiot.live.TimeoutElapsed";

    private static LiveShowApp instance;
    private MediaPlayer mediaPlayer;
    private AlarmTimeout waitTimeout;
    private LiveShowPlayer liveShowPlayer;

    public static void initialize(Context context) {
        instance = new LiveShowApp(context);
    }

    public static void setTestingInstance(LiveShowApp app) {
        instance = app;
    }

    public static void release() {
        instance.releaseInstance();
    }

    public static LiveShowApp getInstance() {
        return instance;
    }

    protected LiveShowApp(Context context) {
        mediaPlayer = new MediaPlayer();
        waitTimeout = new AlarmTimeout(context, TIMEOUT_ELAPSED);
        AudioStream audioStream = new AudioStream(mediaPlayer);
        liveShowPlayer = new LiveShowPlayer(audioStream, waitTimeout);
    }

    private void releaseInstance() {
        mediaPlayer.release();
        waitTimeout.release();
    }

    public LiveShowPlayer getLiveShowPlayer() {
        return liveShowPlayer;
    }

}
