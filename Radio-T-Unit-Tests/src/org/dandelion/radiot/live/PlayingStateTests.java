package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.Playing;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PlayingStateTests {
    private PlaybackContext context = mock(PlaybackContext.class);
    private Playing state = new Playing(context);

    @Test
    public void stopPlaybackGoesStopping() {
        state.stopPlayback();
        verify(context).interrupt();
    }

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback();
        verifyZeroInteractions(context);
    }
}
