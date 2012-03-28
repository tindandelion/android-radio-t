package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayingTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private Playing state = new Playing();

    @Test
    public void togglePlaybackGoesStopping() throws Exception {
        state.togglePlayback(player);
        verify(player).beStopping();
    }

    @Test
    public void onErrorSwitchesToConnecting() throws Exception {
        state.handleError(player);
        verify(player).beConnecting();
    }
}
