package org.dandelion.radiot.live.states;

import android.media.MediaPlayer;

public class Stopping extends PlaybackState implements Runnable {
    public Stopping(MediaPlayer player, PlaybackService service) {
        super(player, service);
    }

    @Override
    public void enter() {
        service.runAsynchronously(this);
    }

    @Override
    public void stopPlayback() {
    }

    @Override
    public void acceptVisitor(PlaybackStateVisitor visitor) {
        visitor.onStopping(this);
    }

    public void run() {
        player.reset();
        service.switchToNewState(new Idle(player, service));
    }

}
