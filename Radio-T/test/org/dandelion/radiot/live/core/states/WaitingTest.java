package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WaitingTest {
    LiveShowPlayer player = mock(LiveShowPlayer.class);
    Waiting state = new Waiting();

    @Test
    public void togglePlaybackGoesIdle() throws Exception {
        state.togglePlayback(player);
        verify(player).beIdle();
    }
}
