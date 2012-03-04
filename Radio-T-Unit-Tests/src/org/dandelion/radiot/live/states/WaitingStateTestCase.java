package org.dandelion.radiot.live.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.Waiting;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class WaitingStateTestCase {
    LiveShowPlayer context = mock(LiveShowPlayer.class);
    Waiting state = new Waiting(context);

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback();
        verifyZeroInteractions(context);
    }

    @Test
    public void stopPlaybackGoesIdle() throws Exception {
        state.stopPlayback();
        verify(context).beIdle();
    }
}
