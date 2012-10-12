package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import org.dandelion.radiot.live.core.AudioStream;

import java.io.IOException;

public class MediaPlayerStream implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, AudioStream {
    private MediaPlayer player;
    private Listener listener;
    private StopTask stopTask;
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
        ensureNoStopTasksExecuting();
        stopTask = new StopTask();
        stopTask.execute();
    }

    private void ensureNoStopTasksExecuting() {
        if (stopTask != null) {
            throw new RuntimeException("Previous stop task hasn't finished yet");
        }
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

    private class StopTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            player.reset();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.onStopped();
            stopTask = null;
        }
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
}

