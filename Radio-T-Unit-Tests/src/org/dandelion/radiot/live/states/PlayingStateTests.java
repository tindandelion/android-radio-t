package org.dandelion.radiot.live.states;

import org.dandelion.radiot.live.core.LiveShowPlayer;
import org.dandelion.radiot.live.core.states.Playing;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PlayingStateTests {
    private LiveShowPlayer context = mock(LiveShowPlayer.class);
    private Playing state = new Playing(context);

    @Test
    public void stopPlaybackGoesStopping() {
        state.stopPlayback();
        verify(context).beStopping();
    }

    @Test
    public void startPlaybackDoesNothing() throws Exception {
        state.startPlayback();
        verifyZeroInteractions(context);
    }
}
