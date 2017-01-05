package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.os.Handler;
import org.dandelion.radiot.live.core.AudioStream;

import java.io.IOException;

public class MediaPlayerStream implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, AudioStream {
    private MediaPlayer player;
    private Listener listener;
    private String url;

    public MediaPlayerStream(String url) {
        this(new MediaPlayer(), url);
    }

    public MediaPlayerStream(MediaPlayer player, String url) {
        this.player = player;
        this.url = url;
        this.listener = new NullStateListener();
        listenForPlayerEvents();
    }

    @Override
    public void release() {
        player.release();
    }

    @Override
    public void setStateListener(Listener listener) {
        this.listener = listener;
        if (this.listener == null) {
            this.listener = new NullStateListener();
        }
    }
    
    @Override
    public void play() throws IOException {
        playUrl(url);
    }

    protected void playUrl(String url) throws IOException {
        player.reset();
        player.setDataSource(url);
        player.prepareAsync();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void stop() {
        final Handler handler = new Handler();
        new StopperThread(handler).start();
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

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        listener.onError();
    }

    private void listenForPlayerEvents() {
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
    }

    private static class NullStateListener implements Listener {
        @Override
        public void onStarted() {
        }

        @Override
        public void onError() {
        }

        @Override
        public void onStopped() {
        }
    }

    private class StopperThread extends Thread {
        private final Handler handler;

        public StopperThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            player.reset();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onStopped();
                }
            });
        }
    }
}

