package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Waiting;

public class WaitingStateTestCase extends PlaybackStateTestCase {
    private Waiting state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Waiting(context);
    }

    public void testCancelTimeoutWhenLeave()
            throws Exception {
        timeoutScheduled = true;
        state.leave();
        assertFalse(timeoutScheduled);
    }
}
