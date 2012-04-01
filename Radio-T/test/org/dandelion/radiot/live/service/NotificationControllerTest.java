package org.dandelion.radiot.live.service;

import org.dandelion.radiot.util.IconNote;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NotificationControllerTest {
    private Foregrounder foregrounder = mock(Foregrounder.class);
    private IconNote note = mock(IconNote.class);
    private String[] statusLabels = new String[] {
            "TestPlaying", "TestConnecting", "TestWaiting"
    };
    private NotificationController controller = new NotificationController(foregrounder, statusLabels, note);
    public static final long TIMESTAMP = 0;

    @Test
    public void goesForegroundWhenConnecting() throws Exception {
        controller.onConnecting(TIMESTAMP);
        verify(foregrounder).startForeground(note);
        verify(note).setText("TestConnecting");
    }

    @Test
    public void goesForegroundWhenPlaying() throws Exception {
        controller.onPlaying(TIMESTAMP);
        verify(foregrounder).startForeground(note);
        verify(note).setText("TestPlaying");
    }

    @Test
    public void goesForegroundWhenWaiting() throws Exception {
        controller.onWaiting(TIMESTAMP);
        verify(foregrounder).startForeground(note);
        verify(note).setText("TestWaiting");
    }

    @Test
    public void goesBackroundWhenIdle() throws Exception {
        controller.onIdle();
        verify(foregrounder).stopForeground();
    }
}
