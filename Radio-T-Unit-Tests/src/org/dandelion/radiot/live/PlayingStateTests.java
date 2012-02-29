package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.Idle;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayingStateTests {
    private PlaybackContext context = mock(PlaybackContext.class);
    private Idle state = new Idle(context);

    @Test
    public void stopPlaybackGoesStopping() {
        state.stopPlayback();
        verify(context).interrupt();
    }
}
