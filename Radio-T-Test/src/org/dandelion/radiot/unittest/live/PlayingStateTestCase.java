package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Playing;

public class PlayingStateTestCase extends PlaybackStateTestCase {

    private Playing state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Playing(null);
    }

    public void testGoesForegroundWhenEntersPlayingState() throws Exception {
        state.enter(service);

        assertTrue(serviceIsForeground);
    }

    public void testLocksWifiWhenEntersState() throws Exception {
        state.enter(service);

        assertTrue(wifiLocked);
    }

    public void testReleasesWifiWhenLeaveState() throws Exception {
        wifiLocked = true;
        state.leave(service);
        assertFalse(wifiLocked);
    }
}
