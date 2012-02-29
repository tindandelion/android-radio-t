package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Playing;
import org.dandelion.radiot.live.core.states.Waiting;

public class ConnectingStateTestCase extends BasicLiveShowStateTestCase {

    private Connecting state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Connecting(context);
    }

    public void testGoesForegroundWhenEntersConnectingState() throws Exception {
        state.enter();
        assertTrue(serviceIsForeground);
    }

    public void testSwitchingToPlayingStateWhenPrepared() throws Exception {
        state.enter();
        player.bePrepared();
        assertSwitchedToState(Playing.class);
    }

    public void testGoesToWaitingStateOnErrorWhilePreparing() throws Exception {
        state.enter();
        player.signalError();
        assertSwitchedToState(Waiting.class);
    }
}
