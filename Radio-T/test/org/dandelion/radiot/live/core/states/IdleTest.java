package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class IdleTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private Idle state = new Idle();

    @Test
    public void startPlaybackInitiatesConnection() throws Exception {
        state.startPlayback(player);
        verify(player).beConnecting();
    }

    @Test
    public void stopPlaybackDoesNothing() throws Exception {
        state.stopPlayback(player);
        verifyZeroInteractions(player);
    }
}
