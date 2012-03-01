package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.states.BasicState;
import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Playing;
import org.dandelion.radiot.live.core.states.Stopping;

public class PlaybackContext implements MediaPlayer.OnPreparedListener {
    public BasicState.ILiveShowService service;
    private MediaPlayer player;

    public PlaybackContext(BasicState.ILiveShowService service, MediaPlayer player) {
        this.service = service;
        this.player = player;
        this.player.setOnPreparedListener(this);
    }

    public void playerSetOnErrorListener(MediaPlayer.OnErrorListener onError) {
        player.setOnErrorListener(onError);
    }

    public void playerReset() {
        player.reset();
    }


    public void serviceSwitchToNewState(BasicState state) {
        service.switchToNewState(state);
    }

    public void serviceGoForeground(int i) {
        service.goForeground(i);
    }

    public void connect() {
        try {
            player.reset();
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

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        play();
    }

    private void play() {
        player.start();
        service.switchToNewState(new Playing(this));
    }
}
