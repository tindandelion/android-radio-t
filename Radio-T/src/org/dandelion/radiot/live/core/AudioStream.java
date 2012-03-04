package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;

public class AudioStream implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer player;
    private StateListener listener;
    private AudioStream.StopTask stopTask;

    public interface StateListener {
        void onStarted();
        void onError();
        void onStopped();
    }

    public AudioStream(MediaPlayer player) {
        this.player = player;
        this.listener = new NullStateListener();
        listenForPlayerEvents();
    }

    public void setStateListener(StateListener listener) {
        this.listener = listener;
        if (this.listener == null) {
            this.listener = new NullStateListener();
        }
    }

    public void play(String url) throws IOException {
        player.reset();
        player.setDataSource(url);
        player.prepareAsync();
    }

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

    private void listenForPlayerEvents() {
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
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
}

class NullStateListener implements AudioStream.StateListener {
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
