package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Idle;

public class IdleStateTestCase extends PlaybackStateTestCase {
    private Idle state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Idle(context);
    }

    public void testGoesBackgroundWhenEntersIdleState() throws Exception {
        serviceIsForeground = true;
        state.enter();

        assertFalse(serviceIsForeground);
    }
}
