package org.dandelion.radiot.live.states;

import android.media.MediaPlayer;

public class Waiting extends PlaybackState {
    private static final int WAITING_NOTIFICATION_STRING_ID = 2;
    public Waiting(MediaPlayer player, PlaybackService service) {
        super(player, service);
    }

    @Override
    public void enter() {
        player.reset();
        service.scheduleTimeout(waitTimeout);
        service.goForeground(WAITING_NOTIFICATION_STRING_ID);
    }

    @Override
    public void leave() {
        service.unscheduleTimeout();
    }

    @Override
    public void acceptVisitor(PlaybackStateVisitor visitor) {
        visitor.onWaiting(this);
    }

    @Override
    public void onTimeout() {
        service.switchToNewState(new Connecting(player, service));
    }
}
