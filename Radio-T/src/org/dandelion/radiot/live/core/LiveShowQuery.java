package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.*;

public interface LiveShowQuery {
    void onWaiting(Waiting state);

    void onIdle(Idle state);

    void onConnecting(Connecting connecting);

    void onPlaying(Playing playing);

    void onStopping(Stopping stopping);
}
