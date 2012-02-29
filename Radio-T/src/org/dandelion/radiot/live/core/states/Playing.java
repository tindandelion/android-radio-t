package org.dandelion.radiot.live.core.states;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Playing extends BasicState {
    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            getPlayer().reset();
            getService().switchToNewState(newConnecting());
            return false;
        }
    };

    public Playing(PlaybackContext context) {
        super(context);
        getPlayer().setOnErrorListener(onError);
    }

    @Override
    public void enter() {
        getService().lockWifi();
        getPlayer().start();
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
}
