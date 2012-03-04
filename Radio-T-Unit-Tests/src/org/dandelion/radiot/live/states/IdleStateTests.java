package org.dandelion.radiot.live.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.Idle;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class IdleStateTests {
    private LiveShowPlayer context = mock(LiveShowPlayer.class);
    private Idle state = new Idle(context);

    @Test
    public void startPlaybackInitiatesConnection() throws Exception {
        state.startPlayback();
        verify(context).beConnecting();
    }

    @Test
    public void stopPlaybackDoesNothing() throws Exception {
        state.stopPlayback();
        verifyZeroInteractions(context);
    }
}
