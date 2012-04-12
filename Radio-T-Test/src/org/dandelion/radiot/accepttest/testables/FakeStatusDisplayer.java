package org.dandelion.radiot.accepttest.testables;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.core.LiveShowStateListener;

import static junit.framework.Assert.assertEquals;

public class FakeStatusDisplayer implements LiveShowStateListener {
    private LiveShowState currentState;

    public void showsStatusFor(LiveShowState expected) {
        assertEquals("Notified status", expected, currentState);
    }

    @Override
    public void onStateChanged(LiveShowState state, long timestamp) {
        currentState = state;
    }
}
