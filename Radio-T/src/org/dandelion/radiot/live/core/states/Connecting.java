package org.dandelion.radiot.live.core.states;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.LiveShowQuery;
import org.dandelion.radiot.live.core.PlaybackContext;

public class Connecting extends BasicState {
    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            context.serviceSwitchToNewState(newWaiting());
            return false;
        }
    };

    public Connecting(PlaybackContext context) {
        super(context);
        this.context.playerSetOnErrorListener(onError);
}

@Override
    public void enter() {
        context.serviceGoForeground(1);
    }

    @Override
    public void acceptVisitor(LiveShowQuery visitor) {
        visitor.onConnecting(this);
    }
}
