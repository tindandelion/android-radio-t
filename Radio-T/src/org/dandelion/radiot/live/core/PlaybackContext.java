package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.states.BasicState;

public class PlaybackContext {
    public BasicState.ILiveShowService service;
    public MediaPlayer player;

    public PlaybackContext(BasicState.ILiveShowService service, MediaPlayer player) {
        this.service = service;
        this.player = player;
    }
}
