package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioStream implements MediaPlayer.OnPreparedListener {
    public interface StateListener {
        void onStarted();
        void onStopped();
    }

    class NullStateListener implements StateListener {
        @Override
        public void onStarted() {
        }

        @Override
        public void onStopped() {
        }
    }

    private MediaPlayer player;
    private String url;
    private StateListener listener;

    public AudioStream(MediaPlayer player, String url) {
        this.url = url;
        this.player = player;
        this.listener = new NullStateListener();
        hookToPlayer();
    }

    public void setStateListener(StateListener listener) {
        this.listener = listener;
        if (this.listener == null) {
            this.listener = new NullStateListener();
        }
    }

    private void hookToPlayer() {
        player.setOnPreparedListener(this);
    }

    public void play() throws IOException {
        player.reset();
        player.setDataSource(url);
        player.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
        listener.onStarted();
   }

    public void stop() {
        player.stop();
        listener.onStopped();
    }
}
