package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Idle;
import org.dandelion.radiot.live.core.states.Stopping;

public class StoppingStateTestCase extends PlaybackStateTestCase {
    public void testEnteringToStoppingStateResetsPlayerAndGoesIdle()
            throws Exception {
        player.bePrepared();

        Stopping state = new Stopping(context);
        state.enter();

        player.assertIsReset();
        assertSwitchedToState(Idle.class);
    }
}
