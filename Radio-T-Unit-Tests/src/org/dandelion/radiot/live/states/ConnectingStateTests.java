package org.dandelion.radiot.live.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.Connecting;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ConnectingStateTests {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private Connecting state = new Connecting();

    @Test
    public void stopPlaybackGoesStopping() throws Exception {
        state.stopPlayback(player);
        verify(player).beStopping();
    }

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback(player);
        verifyZeroInteractions(player);
    }
}
