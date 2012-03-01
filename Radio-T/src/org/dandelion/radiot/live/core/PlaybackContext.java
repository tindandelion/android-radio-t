package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.states.*;

// TODO: Get rid of service.switchToNewState
public class PlaybackContext implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    public PlaybackState.ILiveShowService service;
    private MediaPlayer player;
    private PlaybackState currentState;

    public PlaybackContext(PlaybackState.ILiveShowService service, MediaPlayer player) {
        this.service = service;
        this.player = player;
        this.player.setOnPreparedListener(this);
        this.player.setOnErrorListener(this);
        currentState = new Idle(this);
    }

    public void playerReset() {
        player.reset();
    }


    public void serviceSwitchToNewState(PlaybackState state) {
        setState(state);
    }

    public void serviceGoForeground(int i) {
        service.goForeground(i);
    }

    public void connect() {
        try {
            player.reset();
            player.setDataSource(PlaybackState.liveShowUrl);
            player.prepareAsync();
            setState(new Connecting(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void interrupt() {
        setState(new Stopping(this));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        play();
    }

    public void play() {
        player.start();
        setState(new Playing(this));
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        // TODO: That's a hack, but I want to see how far I can go
        if (currentState instanceof Playing) {
            connect();
        } else {
            waitForNextAttempt();
        }
        return false;
    }

    private void waitForNextAttempt() {
        setState(new Waiting(this));
    }

    private void setState(PlaybackState state) {
        currentState = state;
        service.switchToNewState(currentState);
    }
}
