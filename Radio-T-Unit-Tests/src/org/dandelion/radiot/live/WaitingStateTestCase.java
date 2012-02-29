package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.Waiting;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WaitingStateTestCase {
    PlaybackContext context = mock(PlaybackContext.class);
    Waiting state = new Waiting(context);

    @Test
    public void initiateConnectionAfterTimeout() {
        state.timeoutElapsed();
        verify(context).connect();
    }
}
