package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectingTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private Connecting state = new Connecting();

    @Test
    public void togglePlaybackGoesStopping() throws Exception {
        state.togglePlayback(player);
        verify(player).beStopping();
    }

    @Test
    public void onErrorGoesWaiting() throws Exception {
        state.handleError(player);
        verify(player).beWaiting();
    }
}
