package org.dandelion.radiot.live.core;

public interface LiveShowStateListener {
    void onStateChanged(LiveShowState state, long timestamp);
}
