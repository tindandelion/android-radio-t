package org.dandelion.radiot.live.core.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class StoppingTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private Stopping state = new Stopping();

    @Test
    public void togglePlaybackDoesNothing() throws Exception {
        state.togglePlayback(player);
        verifyZeroInteractions(player);
    }
}
