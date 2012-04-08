package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.LiveShowState;

public interface LiveShowStateListener {
    void onStateChanged(LiveShowState state, long timestamp);
}
