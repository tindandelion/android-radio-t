package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Idle;

public class IdleStateTestCase extends PlaybackStateTestCase {
    private Idle state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Idle(null);
    }

    public void testGoesBackgroundWhenEntersIdleState() throws Exception {
        serviceIsForeground = true;
        state.enter(service);

        assertFalse(serviceIsForeground);
    }
}
