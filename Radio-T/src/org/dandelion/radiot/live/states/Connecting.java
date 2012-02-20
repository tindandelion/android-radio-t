package org.dandelion.radiot.live.states;

import android.media.MediaPlayer;

public class Connecting extends PlaybackState {
    private MediaPlayer.OnPreparedListener onPrepared = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            service.switchToNewState(new Playing(player, service));
        }
    };
    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            service.switchToNewState(new Waiting(player, service));
            return false;
        }
    };

    public Connecting(MediaPlayer player, PlaybackService service) {
        super(player, service);
        player.setOnPreparedListener(onPrepared);
        player.setOnErrorListener(onError);
    }

    @Override
    public void enter() {
        try {
            player.reset();
            player.setDataSource(liveShowUrl);
            player.prepareAsync();
            service.goForeground(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acceptVisitor(PlaybackStateVisitor visitor) {
        visitor.onConnecting(this);
    }
}
