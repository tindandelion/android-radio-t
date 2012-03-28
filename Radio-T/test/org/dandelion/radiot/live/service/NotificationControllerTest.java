package org.dandelion.radiot.live.service;

import android.app.Notification;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NotificationControllerTest {
    private Foregrounder foregrounder = mock(Foregrounder.class);
    private Notification note = mock(Notification.class);
    private NotificationBuilder builder = mock(NotificationBuilder.class);
    private String[] statusLabels = new String[] {
            "TestPlaying", "TestConnecting", "TestWaiting"
    };
    private NotificationController controller = new NotificationController(foregrounder, builder, statusLabels);
    public static final long TIMESTAMP = 0;

    @Test
    public void goesForegroundWhenConnecting() throws Exception {
        when(builder.createNotification("TestConnecting")).thenReturn(note);
        controller.onConnecting(TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesForegroundWhenPlaying() throws Exception {
        when(builder.createNotification("TestPlaying")).thenReturn(note);
        controller.onPlaying(TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesForegroundWhenWaiting() throws Exception {
        when(builder.createNotification("TestWaiting")).thenReturn(note);
        controller.onWaiting(TIMESTAMP);
        verify(foregrounder).startForeground(note);
    }

    @Test
    public void goesBackroundWhenIdle() throws Exception {
        controller.onIdle();
        verify(foregrounder).stopForeground();
    }
}
