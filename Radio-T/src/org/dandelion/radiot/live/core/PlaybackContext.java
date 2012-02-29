package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.states.BasicState;

import java.io.IOException;

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

    public void playerSetDataSource(String url) throws IOException {
        player.setDataSource(url);
    }

    public void playerPrepareAsync() {
        player.prepareAsync();
    }

    public void playerStart() {
        player.start();
    }
}
