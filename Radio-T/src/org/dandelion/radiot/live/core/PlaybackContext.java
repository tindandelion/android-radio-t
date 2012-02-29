package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.states.BasicState;
import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Stopping;

public class PlaybackContext {
    public BasicState.ILiveShowService service;
    private MediaPlayer player;

    public PlaybackContext(BasicState.ILiveShowService service, MediaPlayer player) {
        this.service = service;
        this.player = player;
    }

    public void playerSetOnPreparedListener(MediaPlayer.OnPreparedListener onPrepared) {
        player.setOnPreparedListener(onPrepared);
    }

    public void playerSetOnErrorListener(MediaPlayer.OnErrorListener onError) {
        player.setOnErrorListener(onError);
    }

    public void playerReset() {
        player.reset();
    }


    public void playerStart() {
        player.start();
    }

    public void serviceSwitchToNewState(BasicState state) {
        service.switchToNewState(state);
    }

    public void serviceGoForeground(int i) {
        service.goForeground(i);
    }

    public void connect() {
        try {
            player.setDataSource(BasicState.liveShowUrl);
            player.prepareAsync();
            service.switchToNewState(new Connecting(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void interrupt() {
        service.switchToNewState(new Stopping(this));
    }
}
