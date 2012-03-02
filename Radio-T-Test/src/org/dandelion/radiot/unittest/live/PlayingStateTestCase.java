package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Playing;

public class PlayingStateTestCase extends PlaybackStateTestCase {

    private Playing state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Playing(context);
    }

    public void testGoesForegroundWhenEntersPlayingState() throws Exception {
        player.bePrepared();
        state.enter();

        assertTrue(serviceIsForeground);
    }

    public void testLocksWifiWhenEntersState() throws Exception {
        player.bePrepared();
        state.enter();

        assertTrue(wifiLocked);
    }

    public void testReleasesWifiWhenLeaveState() throws Exception {
        wifiLocked = true;
        state.leave();
        assertFalse(wifiLocked);
    }
}
