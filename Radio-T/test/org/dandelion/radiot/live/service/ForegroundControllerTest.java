package org.dandelion.radiot.live.service;

import org.dandelion.radiot.util.IconNote;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ForegroundControllerTest {
    private Foregrounder foregrounder = mock(Foregrounder.class);
    private IconNote note = mock(IconNote.class);
    private ForegroundController controller = new ForegroundController(foregrounder, note);
    public static final long TIMESTAMP = 0;

    @Test
    public void goesForegroundWhenConnecting() throws Exception {
        controller.onConnecting(TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesForegroundWhenPlaying() throws Exception {
        controller.onPlaying(TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesForegroundWhenWaiting() throws Exception {
        controller.onWaiting(TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesBackroundWhenIdle() throws Exception {
        controller.onIdle();
        verify(foregrounder).stopForeground();
    }
}
