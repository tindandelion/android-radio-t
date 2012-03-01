package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.BasicState;
import org.dandelion.radiot.live.core.states.Connecting;
import org.dandelion.radiot.live.core.states.Playing;
import org.dandelion.radiot.live.core.states.Stopping;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlaybackContextTests {

    private MediaPlayer player = mock(MediaPlayer.class);
    private BasicState.ILiveShowService service = mock(BasicState.ILiveShowService.class);
    private PlaybackContext context;
    private MediaPlayer.OnPreparedListener preparedListener;

    @Before
    public void setUp() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                preparedListener = (MediaPlayer.OnPreparedListener) invocation.getArguments()[0];
                return null;
            }
        }).when(player).setOnPreparedListener(any(MediaPlayer.OnPreparedListener.class));
        context = new PlaybackContext(service, player);
    }

    @Test
    public void connectSwitchesToConnectingState() throws Exception {
        context.connect();
        verify(service).switchToNewState(isA(Connecting.class));
    }

    @Test
    public void connectInitializesMediaPlayer() throws Exception {
        context.connect();
        verify(player).reset();
        verify(player).setDataSource(BasicState.liveShowUrl);
        verify(player).prepareAsync();
    }

    @Test
    public void interruptSwitchesToStoppingState() throws Exception {
        context.interrupt();
        verify(service).switchToNewState(isA(Stopping.class));
    }

    @Test
    public void goesPlayingWhenPrepared() throws Exception {
        context.connect();
        preparedListener.onPrepared(player);
        
        verify(player).start();
        verify(service).switchToNewState(isA(Playing.class));
    }

    // TODO: Error handling for connecting and playing states
}
