package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.LiveShowState;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WaitingTest {
    private final LiveShowPlayer player = mock(LiveShowPlayer.class);
    private final LiveShowState state = LiveShowState.Waiting;

    @Test
    public void togglePlaybackGoesIdle() throws Exception {
        state.togglePlayback(player);
        verify(player).beIdle();
    }
}
