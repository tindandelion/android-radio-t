package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.states.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

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
    public void connectInitiatesPlaying() throws Exception {
        // TODO: Rename method connect()
        context.connect();
        verifyIsConnecting();
    }

    @Test
    public void interruptInitiatesStopping() throws Exception {
        context.interrupt();
        verifyIsStopping();
    }

    @Test
    public void goesPlayingWhenPrepared() throws Exception {
        context.connect();
        audioStateListener.onStarted();
        verifyIsPlaying();
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
        verifyIsConnecting();
    }

    @Test
    public void normalPlaybackWorkflow() throws Exception {
        context.startPlayback();
        verifyIsConnecting();
        audioStateListener.onStarted();
        verifyIsPlaying();

        context.stopPlayback();
        verifyIsStopping();
        audioStateListener.onStopped();
        verifyIsIdle();
    }

    private void verifyIsIdle() {
        verify(service).switchToNewState(isA(Idle.class));
    }

    private void verifyIsConnecting() throws IOException {
        verify(audioStream).play(PlaybackContext.liveShowUrl);
        verify(service).switchToNewState(isA(Connecting.class));
    }

    private void verifyIsStopping() {
        verify(audioStream).stop();
        verify(service).switchToNewState(isA(Stopping.class));
    }

    private void verifyIsPlaying() {
        verify(service).switchToNewState(isA(Playing.class));
    }
}
