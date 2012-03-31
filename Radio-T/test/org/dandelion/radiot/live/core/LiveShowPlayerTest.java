package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LiveShowPlayerTest {
    private final LiveShowStateHolder stateHolder = new LiveShowStateHolder(new Idle());
    private final AudioStream audioStream = mock(AudioStream.class);
    private final Timeout timeout = mock(Timeout.class);
    private LiveShowPlayer player;

    private Runnable timeoutCallback;
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

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                timeoutCallback = (Runnable) invocation.getArguments()[1];
                return null;
            }
        }).when(timeout).set(anyInt(), any(Runnable.class));

        player = new LiveShowPlayer(audioStream, stateHolder, timeout);
    }

    @Test
    public void goesConnecting() throws Exception {
        player.beConnecting();
        verifyIsConnecting();
    }

    @Test
    public void goesStopping() throws Exception {
        player.beStopping();
        verifyIsStopping();
    }

    @Test
    public void goesPlayingWhenPrepared() throws Exception {
        player.beConnecting();
        audioStateListener.onStarted();
        verifyIsPlaying();
    }

    @Test
    public void goesWaitingOnPrepareError() throws Exception {
        player.beConnecting();
        audioStateListener.onError();
        verifySwitchedToState(Waiting.class);
    }

    @Test
    public void goesConnectingOnPlayingError() throws Exception {
        player.onStarted();
        audioStateListener.onError();
        verifyIsConnecting();
    }

    @Test
    public void goesIdleOnStopped() throws Exception {
        player.beStopping();
        audioStateListener.onStopped();
        verifyIsIdle();
    }

    @Test
    public void reconnectWhenWaitTimeoutElapses() throws Exception {
        player.beWaiting();
        verifyIsWaiting();
        timeoutCallback.run();
        verifyIsConnecting();
    }

    @Test
    public void resetsTimeoutWhenGoesIdle() throws Exception {
        player.beIdle();
        verify(timeout).reset();
    }

    @Test
    public void normalPlaybackWorkflow() throws Exception {
        player.togglePlayback();
        verifyIsConnecting();
        audioStateListener.onStarted();
        verifyIsPlaying();

        player.togglePlayback();
        verifyIsStopping();
        audioStateListener.onStopped();
        verifyIsIdle();
    }

    @Test
    public void waitingForShowWorkflow() throws Exception {
        player.togglePlayback();
        verifyIsConnecting();
        audioStateListener.onError();
        verifyIsWaiting();
    }

    private void verifyIsWaiting() {
        verify(timeout).set(eq(LiveShowPlayer.WAIT_TIMEOUT), any(Runnable.class));
        verifySwitchedToState(Waiting.class);
    }

    private void verifyIsIdle() {
        verifySwitchedToState(Idle.class);
    }

    private void verifyIsConnecting() throws IOException {
        verify(audioStream).play();
        verifySwitchedToState(Connecting.class);
    }

    private void verifySwitchedToState(Class<?> expected) {
        assertEquals(expected, stateHolder.value().getClass());
    }

    private void verifyIsStopping() {
        verify(audioStream).stop();
        verifySwitchedToState(Stopping.class);
    }

    private void verifyIsPlaying() {
        verifySwitchedToState(Playing.class);
    }
}
