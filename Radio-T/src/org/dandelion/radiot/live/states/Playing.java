package org.dandelion.radiot.live.states;

import android.media.MediaPlayer;

/**
* Created by IntelliJ IDEA.
* User: moshse
* Date: 2/20/12
* Time: 5:41 PM
* To change this template use File | Settings | File Templates.
*/
public class Playing extends PlaybackState {
    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            player.reset();
            service.switchToNewState(new Connecting(player, service));
            return false;
        }
    };

    public Playing(MediaPlayer player, PlaybackService service) {
        super(player, service);
        player.setOnErrorListener(onError);
    }

    @Override
    public void enter() {
        service.lockWifi();
        player.start();
        service.goForeground(0);
    }

    @Override
    public void leave() {
        service.unlockWifi();
    }

    @Override
    public void acceptVisitor(PlaybackStateVisitor visitor) {
        visitor.onPlaying(this);
    }
}
