package org.dandelion.radiot.live;

import android.media.MediaPlayer;

public class LiveShowApp {
    private static LiveShowApp instance;
    private MediaPlayer mediaPlayer;

    public static void initialize() {
        instance = new LiveShowApp();
    }

    public static void release() {
        instance.releaseInstance();
    }

    public static LiveShowApp getInstance() {
        return instance;
    }

    private LiveShowApp() {
        mediaPlayer = new MediaPlayer();
    }

    private void releaseInstance() {
        mediaPlayer.release();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
