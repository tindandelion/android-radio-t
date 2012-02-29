package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Waiting;

public class WaitingStateTestCase extends BasicLiveShowStateTestCase {
    private Waiting state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Waiting(context);
    }

    public void testWaitsForTimeoutAndGoesToConnectingState() throws Exception {
        state.enter();
        assertTrue(timeoutScheduled);
        state.timeoutElapsed();
        assertSwitchedToState(Connecting.class);
    }

    public void testResetsPlayerWhenEntersWaitingState() throws Exception {
        player.prepareAsync();
        state.enter();
        player.assertIsReset();
    }

    public void testCancelTimeoutWhenLeave()
            throws Exception {
        timeoutScheduled = true;
        state.leave();
        assertFalse(timeoutScheduled);
    }
}
