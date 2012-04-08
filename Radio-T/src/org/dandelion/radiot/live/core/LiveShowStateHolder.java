package org.dandelion.radiot.live.core;

import java.util.ArrayList;
import java.util.List;

public class LiveShowStateHolder {
    private LiveShowState value;
    private final List<LiveShowStateListener> listeners = new ArrayList<LiveShowStateListener>();
    private long timestamp;

    public static LiveShowStateHolder initial() {
        return new LiveShowStateHolder(LiveShowState.Idle, 0);
    }

    public LiveShowStateHolder(LiveShowState initialState, long timestamp) {
        this.value = initialState;
        this.timestamp = timestamp;
    }

    public LiveShowState value() {
        return value;
    }

    public void setValue(LiveShowState newValue, long newTimestamp) {
        value = newValue;
        timestamp = newTimestamp;
        notifyListeners();
    }


    private void notifyListeners() {
        for (LiveShowStateListener listener : listeners) {
            notifyListener(listener);
        }
    }

    private void notifyListener(LiveShowStateListener listener) {
        listener.onStateChanged(value, timestamp);
    }

    public void addListener(LiveShowStateListener listener) {
        listeners.add(listener);
        notifyListener(listener);
    }

    public void removeListener(LiveShowStateListener listener) {
        listeners.remove(listener);
    }

}
