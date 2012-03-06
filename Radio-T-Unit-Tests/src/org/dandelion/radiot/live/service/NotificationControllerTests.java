package org.dandelion.radiot.live.service;

import android.app.Notification;
import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Idle;
import org.dandelion.radiot.live.core.states.Playing;
import org.dandelion.radiot.live.core.states.Waiting;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NotificationControllerTests {
    private static int NOTIFICATION_ID = 1111;
    private Foregrounder foregrounder = mock(Foregrounder.class);
    private Notification note = mock(Notification.class);
    private NotificationBuilder builder = mock(NotificationBuilder.class);
    private String[] statusLabels = new String[] {
            "TestPlaying", "TestConnecting", "TestWaiting"
    };
    private NotificationController controller = new NotificationController(NOTIFICATION_ID, foregrounder, builder, statusLabels);

    @Test
    public void goesForegroundWhenConnecting() throws Exception {
        when(builder.createNotification("TestConnecting")).thenReturn(note);
        controller.onConnecting(new Connecting(null));
        verify(foregrounder).startForeground(NOTIFICATION_ID, note);
    }

    @Test
    public void goesForegroundWhenPlaying() throws Exception {
        when(builder.createNotification("TestPlaying")).thenReturn(note);
        controller.onPlaying(new Playing(null));
        verify(foregrounder).startForeground(NOTIFICATION_ID, note);
    }

    @Test
    public void goesForegroundWhenWaiting() throws Exception {
        when(builder.createNotification("TestWaiting")).thenReturn(note);
        controller.onWaiting(new Waiting(null));
        verify(foregrounder).startForeground(NOTIFICATION_ID, note);
    }

    @Test
    public void goesBackroundWhenIdle() throws Exception {
        controller.onIdle(new Idle(null));
        verify(foregrounder).stopForeground();
    }
}
