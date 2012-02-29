package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.BasicState;
import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Stopping;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlaybackContextTests {

    private MediaPlayer player = mock(MediaPlayer.class);
    private BasicState.ILiveShowService service = mock(BasicState.ILiveShowService.class);
    private PlaybackContext context = new PlaybackContext(service, player);

    @Test
    public void connectSwitchesToConnectingState() throws Exception {
        context.connect();
        verify(service).switchToNewState(isA(Connecting.class));
    }

    @Test
    public void connectInitializesMediaPlayer() throws Exception {
        context.connect();
        verify(player).setDataSource(BasicState.liveShowUrl);
        verify(player).prepareAsync();
    }

    @Test
    public void interruptSwitchesToStoppingState() throws Exception {
        context.interrupt();
        verify(service).switchToNewState(isA(Stopping.class));
    }
}
