package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioStream implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

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

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        listener.onError();
        return false;
    }

    // TODO: Review usages
    public void reset() {
        player.reset();
    }

    public interface StateListener {
        void onStarted();
        void onError();
    }

    class NullStateListener implements StateListener {
        @Override
        public void onStarted() {
        }

        @Override
        public void onError() {
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
        player.setOnErrorListener(this);
    }

    public void setStateListener(StateListener listener) {
        this.listener = listener;
        if (this.listener == null) {
            this.listener = new NullStateListener();
        }
    }
}
