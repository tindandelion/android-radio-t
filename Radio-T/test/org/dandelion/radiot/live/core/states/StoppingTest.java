package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class StoppingTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private Stopping state = new Stopping();

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback(player);
        verifyZeroInteractions(player);
    }

    @Test
    public void stopPlaybackDoesNothing() throws Exception {
        state.stopPlayback(player);
        verifyZeroInteractions(player);
    }
}
