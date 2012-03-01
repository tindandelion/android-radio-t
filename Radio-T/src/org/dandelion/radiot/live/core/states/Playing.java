package org.dandelion.radiot.live.core.states;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Playing extends BasicState implements MediaPlayer.OnErrorListener {
    public Playing(PlaybackContext context) {
        super(context);
        this.context.playerSetOnErrorListener(this);
    }

    @Override
    public void enter() {
        getService().lockWifi();
        getService().goForeground(0);
    }

    @Override
    public void leave() {
        getService().unlockWifi();
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onPlaying(this);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        context.connect();
        return false;
    }
}
