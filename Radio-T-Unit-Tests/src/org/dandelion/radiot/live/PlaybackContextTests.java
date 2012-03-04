package org.dandelion.radiot.live;

import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.PlaybackContext;
import org.dandelion.radiot.live.core.Timeout;
import org.dandelion.radiot.live.core.states.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PlaybackContextTests implements PlaybackContext.PlaybackStateListener {
    private PlaybackContext context;
    private AudioStream audioStream = mock(AudioStream.class);
    private AudioStream.StateListener audioStateListener;
    private PlaybackState switchedState = new PlaybackState(null);
    private Timeout timeout = mock(Timeout.class);
    private Runnable timeoutCallback;

    @Before
    public void setUp() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                audioStateListener = (AudioStream.StateListener) invocation.getArguments()[0];
                return null;
            }
        }).when(audioStream).setStateListener(any(AudioStream.StateListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                timeoutCallback = (Runnable) invocation.getArguments()[1];
                return null;
            }
        }).when(timeout).set(anyInt(), any(Runnable.class));

        PlaybackState.ILiveShowService service = mock(PlaybackState.ILiveShowService.class);
        context = new PlaybackContext(service, audioStream, timeout);
        context.setListener(this);
    }

    @Test
    public void goesConnecting() throws Exception {
        context.beConnecting();
        verifyIsConnecting();
    }

    @Test
    public void goesStopping() throws Exception {
        context.beStopping();
        verifyIsStopping();
    }

    @Test
    public void goesPlayingWhenPrepared() throws Exception {
        context.beConnecting();
        audioStateListener.onStarted();
        verifyIsPlaying();
    }

    @Test
    public void goesWaitingOnPrepareError() throws Exception {
        context.beConnecting();
        audioStateListener.onError();
        verifySwitchedToState(Waiting.class);
    }

    @Test
    public void goesConnectingOnPlayingError() throws Exception {
        context.onStarted();
        audioStateListener.onError();
        verifyIsConnecting();
    }

    @Test
    public void goesIdleOnStopped() throws Exception {
        context.beStopping();
        audioStateListener.onStopped();
        verifyIsIdle();
    }

    @Test
    public void reconnectWhenWaitTimeoutElapses() throws Exception {
        context.beWaiting();
        verifyIsWaiting();
        timeoutCallback.run();
        verifyIsConnecting();
    }

    @Test
    public void resetsTimeoutWhenGoesIdle() throws Exception {
        context.beIdle();
        verify(timeout).reset();
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

    @Test
    public void waitingForShowWorkflow() throws Exception {
        context.startPlayback();
        verifyIsConnecting();
        audioStateListener.onError();
        verifyIsWaiting();
    }

    private void verifyIsWaiting() {
        verify(timeout).set(eq(PlaybackState.waitTimeout), any(Runnable.class));
        verifySwitchedToState(Waiting.class);
    }

    private void verifyIsIdle() {
        verifySwitchedToState(Idle.class);
    }

    private void verifyIsConnecting() throws IOException {
        verify(audioStream).play(PlaybackContext.liveShowUrl);
        verifySwitchedToState(Connecting.class);
    }

    private void verifySwitchedToState(Class<?> expected) {
        assertEquals(expected, switchedState.getClass());
    }

    private void verifyIsStopping() {
        verify(audioStream).stop();
        verifySwitchedToState(Stopping.class);
    }

    private void verifyIsPlaying() {
        verifySwitchedToState(Playing.class);
    }

    @Override
    public void onChangedState(PlaybackState oldState, PlaybackState newState) {
        switchedState = newState;
    }
}
