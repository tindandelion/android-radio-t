package org.dandelion.radiot.unittest.live;

import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Playing;
import org.dandelion.radiot.live.core.states.Stopping;

public class PlayingStateTestCase extends PlaybackStateTestCase {

    private Playing state;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new Playing(context);
    }

    public void testStartsPlaybackWhenEntersPlayingState() throws Exception {
        player.bePrepared();
        state.enter();

        player.assertIsPlaying(null);
    }

    public void testGoesToStoppingStateWhenStopsPlayback() throws Exception {
        state.stopPlayback();
        assertSwitchedToState(Stopping.class);
    }

    public void testGoesForegroundWhenEntersPlayingState() throws Exception {
        player.bePrepared();
        state.enter();

        assertTrue(serviceIsForeground);
    }

    public void testSwitchingToConnectingStateOnPlaybackError()
            throws Exception {
        player.bePrepared();
        state.enter();
        player.signalError();
        assertSwitchedToState(Connecting.class);

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
