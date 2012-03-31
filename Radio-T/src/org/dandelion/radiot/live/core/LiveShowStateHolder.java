package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.LiveShowState;

public class LiveShowStateHolder {
    private LiveShowState value;

    public LiveShowStateHolder(LiveShowState initial) {
        value = initial;
    }

    public LiveShowState value() {
        return value;
    }

    public void setValue(LiveShowState newValue) {
        value = newValue;
    }
}
