package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.AudioStream;
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

    private PlaybackState.ILiveShowService service = mock(PlaybackState.ILiveShowService.class);
    private PlaybackContext context;
    private AudioStream audioStream = mock(AudioStream.class);
    private AudioStream.StateListener audioStateListener;

    @Before
    public void setUp() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                audioStateListener = (AudioStream.StateListener) invocation.getArguments()[0];
                return null;
            }
        }).when(audioStream).setStateListener(any(AudioStream.StateListener.class));

        context = new PlaybackContext(service, audioStream);
    }

    @Test
    public void connectSwitchesToConnectingState() throws Exception {
        context.connect();
        verify(service).switchToNewState(isA(Connecting.class));
    }

    @Test
    public void connectStartsPlaying() throws Exception {
        // TODO: Rename method connect()
        context.connect();
        verify(audioStream).play(PlaybackContext.liveShowUrl);
    }

    @Test
    public void interruptSwitchesToStoppingState() throws Exception {
        context.interrupt();
        verify(service).switchToNewState(isA(Stopping.class));
    }

    @Test
    public void goesPlayingWhenPrepared() throws Exception {
        context.connect();
        audioStateListener.onStarted();
        
        verify(service).switchToNewState(isA(Playing.class));
    }

    @Test
    public void goesWaitingOnPrepareError() throws Exception {
        context.connect();
        audioStateListener.onError();
        verify(service).switchToNewState(isA(Waiting.class));
    }

    @Test
    public void goesConnectingOnPlayingError() throws Exception {
        context.onStarted();
        audioStateListener.onError();
        verify(service).switchToNewState(isA(Connecting.class));
        verify(audioStream).play(PlaybackContext.liveShowUrl);
    }
}
