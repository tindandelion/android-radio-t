package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioStream implements MediaPlayer.OnPreparedListener {

    public void play(String url) throws IOException {
        player.reset();
        player.setDataSource(url);
        player.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
        listener.onStarted();
    }

    public interface StateListener {
        void onStarted();
    }

    class NullStateListener implements StateListener {
        @Override
        public void onStarted() {
        }
    }

    private MediaPlayer player;
    private StateListener listener;

    public AudioStream(MediaPlayer player) {
        this.player = player;
        this.listener = new NullStateListener();
        listenForPlayerEvents();
    }

    private void listenForPlayerEvents() {
        player.setOnPreparedListener(this);
    }

    public void setStateListener(StateListener listener) {
        this.listener = listener;
        if (this.listener == null) {
            this.listener = new NullStateListener();
        }
    }
}
