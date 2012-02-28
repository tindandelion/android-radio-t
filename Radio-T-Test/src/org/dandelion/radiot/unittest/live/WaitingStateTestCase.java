package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.LiveShowState;

public class WaitingStateTestCase extends BasicLiveShowStateTestCase {
    private LiveShowState.Waiting state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new LiveShowState.Waiting(player, service);
    }

    public void testWaitsForTimeoutAndGoesToConnectingState() throws Exception {
        state.enter();
        assertTrue(timeoutScheduled);
        state.timeoutElapsed();
        assertSwitchedToState(LiveShowState.Connecting.class);
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
