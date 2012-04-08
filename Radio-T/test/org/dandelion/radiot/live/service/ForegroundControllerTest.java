package org.dandelion.radiot.live.service;

import org.dandelion.radiot.live.core.LiveShowState;
import org.dandelion.radiot.util.IconNote;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ForegroundControllerTest {
    private Foregrounder foregrounder = mock(Foregrounder.class);
    private IconNote note = mock(IconNote.class);
    private ForegroundController controller = new ForegroundController(foregrounder, note);
    public static final long UNUSED_TIMESTAMP = 0;

    @Test
    public void goesForegroundWhenConnecting() throws Exception {
        controller.onStateChanged(LiveShowState.Connecting, UNUSED_TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesForegroundWhenPlaying() throws Exception {
        controller.onStateChanged(LiveShowState.Playing, UNUSED_TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesForegroundWhenWaiting() throws Exception {
        controller.onStateChanged(LiveShowState.Waiting, UNUSED_TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesBackroundWhenIdle() throws Exception {
        controller.onStateChanged(LiveShowState.Idle, UNUSED_TIMESTAMP);
        verify(foregrounder).stopForeground();
    }
}
