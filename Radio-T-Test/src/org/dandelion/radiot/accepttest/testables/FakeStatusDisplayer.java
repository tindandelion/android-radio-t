package org.dandelion.radiot.accepttest.testables;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.live.ui.LiveStatusDisplayer;

import static junit.framework.Assert.assertEquals;

public class FakeStatusDisplayer implements LiveStatusDisplayer {
    private LiveShowState currentState;

    @Override
    public void showStatus(LiveShowState state) {
        currentState = state;
    }

    public void showsStatusFor(LiveShowState expected) {
        assertEquals("Notified status", expected, currentState);
    }
}
