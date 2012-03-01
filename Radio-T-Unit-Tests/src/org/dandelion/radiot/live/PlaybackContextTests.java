package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.*;
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
    private PlaybackState.ILiveShowService service = mock(PlaybackState.ILiveShowService.class);
    private PlaybackContext context;
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer.OnErrorListener errorListener;

    @Before
    public void setUp() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                preparedListener = (MediaPlayer.OnPreparedListener) invocation.getArguments()[0];
                return null;
            }
        }).when(player).setOnPreparedListener(any(MediaPlayer.OnPreparedListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                errorListener = (MediaPlayer.OnErrorListener) invocation.getArguments()[0];
                return null;
            }
        }).when(player).setOnErrorListener(any(MediaPlayer.OnErrorListener.class));

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
        verify(player).setDataSource(PlaybackState.liveShowUrl);
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

    @Test
    public void goesWaitingOnPrepareError() throws Exception {
        context.connect();
        errorListener.onError(player, 0, 0);
        verify(service).switchToNewState(isA(Waiting.class));
    }

    @Test
    public void goesConnectingOnPlayingError() throws Exception {
        context.play();
        errorListener.onError(player, 0, 0);

        // TODO: Stupid check that we actually connect, not simply changing state
        verify(service).switchToNewState(isA(Connecting.class));
        verify(player).prepareAsync();
    }
}
