package org.dandelion.radiot.live.states;

import android.media.MediaPlayer;

public class Idle extends PlaybackState {
    public Idle(MediaPlayer player, PlaybackService service) {
        super(player, service);
        player.setOnErrorListener(null);
    }

    @Override
    public void enter() {
        service.goBackground();
    }

    @Override
    public void startPlayback() {
        service.switchToNewState(new Connecting(player, service));
    }

    @Override
    public void acceptVisitor(PlaybackStateVisitor visitor) {
        visitor.onIdle(this);
    }
}
