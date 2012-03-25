package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class WaitingTest {
    LiveShowPlayer player = mock(LiveShowPlayer.class);
    Waiting state = new Waiting();

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback(player);
        verifyZeroInteractions(player);
    }

    @Test
    public void stopPlaybackGoesIdle() throws Exception {
        state.stopPlayback(player);
        verify(player).beIdle();
    }
}
