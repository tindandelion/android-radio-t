package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.LiveShowState;

public class LiveShowStateHolder {
    private LiveShowState value;
    private LiveShowStateListener listener;

    public LiveShowStateHolder(LiveShowState initial) {
        value = initial;
    }

    public LiveShowStateHolder() {
        this(null);
    }

    public LiveShowState value() {
        return value;
    }

    public void setValue(LiveShowState newValue) {
        value = newValue;
        notifyListener();
    }

    private void notifyListener() {
        if (listener != null) {
            listener.onStateChanged(value);
        }
    }

    public void setListener(LiveShowStateListener listener) {
        this.listener = listener;
    }
}
