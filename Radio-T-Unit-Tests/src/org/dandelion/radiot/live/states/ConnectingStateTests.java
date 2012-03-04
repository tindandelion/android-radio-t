package org.dandelion.radiot.live.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.Connecting;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectingStateTests {
    private LiveShowPlayer context = mock(LiveShowPlayer.class);
    private Connecting state = new Connecting(context);

    @Test
    public void stopPlaybackGoesIdle() throws Exception {
        state.stopPlayback();
        verify(context).beStopping();
    }
}
