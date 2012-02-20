package org.dandelion.radiot.live.states;

public interface PlaybackStateVisitor {
    void onWaiting(Waiting state);

    void onIdle(Idle state);

    void onConnecting(Connecting connecting);

    void onPlaying(Playing playing);

    void onStopping(Stopping stopping);
}
