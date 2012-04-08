package org.dandelion.radiot.live.core;

import org.dandelion.radiot.live.core.states.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LiveShowPlayerTest {
    private final LiveShowStateHolder stateHolder = LiveShowStateHolder.initial();
    private final AudioStream audioStream = mock(AudioStream.class);
    private LiveShowPlayer player;

    private AudioStream.StateListener audioStateListener;
    private Scheduler schedule = mock(Scheduler.class);


    @Before
    public void setUp() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                audioStateListener = (AudioStream.StateListener) invocation.getArguments()[0];
                return null;
            }
        }).when(audioStream).setStateListener(any(AudioStream.StateListener.class));

        player = new LiveShowPlayer(audioStream, stateHolder, schedule);
    }

    @Test
    public void goesConnecting() throws Exception {
        player.beConnecting();
        assertIsConnecting();
    }

    @Test
    public void goesStopping() throws Exception {
        player.beStopping();
        assertIsStopping();
    }

    @Test
    public void goesPlayingWhenPrepared() throws Exception {
        player.beConnecting();
        audioStateListener.onStarted();
        assertIsPlaying();
    }

    @Test
    public void schedulesTheNextAttemptIfConnectFails() throws Exception {
        player.beConnecting();
        audioStateListener.onError();
        assertCurrentStateIs(Waiting.class);
        verify(schedule).scheduleNextAttempt();
    }

    @Test
    public void goesConnectingOnPlayingError() throws Exception {
        player.onStarted();
        audioStateListener.onError();
        assertIsConnecting();
    }

    @Test
    public void goesIdleOnStopped() throws Exception {
        player.beStopping();
        audioStateListener.onStopped();
        assertIsIdle();
    }

    @Test
    public void performNextConnectionAttempt() throws Exception {
        player.beWaiting();
        player.performNextAttempt();
        assertIsConnecting();
    }

    @Test
    public void cancelsConnectionAttemptsWhenGoesIdle() throws Exception {
        player.beIdle();
        verify(schedule).cancelAttempts();
    }

    @Test
    public void normalPlaybackWorkflow() throws Exception {
        player.togglePlayback();
        assertIsConnecting();
        audioStateListener.onStarted();
        assertIsPlaying();

        player.togglePlayback();
        assertIsStopping();
        audioStateListener.onStopped();
        assertIsIdle();
    }

    @Test
    public void waitingForShowWorkflow() throws Exception {
        player.togglePlayback();
        assertIsConnecting();
        audioStateListener.onError();
        assertCurrentStateIs(Waiting.class);
    }

    private void assertIsIdle() {
        assertCurrentStateIs(Idle.class);
    }

    private void assertIsConnecting() throws IOException {
        verify(audioStream).play();
        assertCurrentStateIs(Connecting.class);
    }

    private void assertCurrentStateIs(Class<?> expected) {
        assertEquals(expected, stateHolder.value().getClass());
    }

    private void assertIsStopping() {
        verify(audioStream).stop();
        assertCurrentStateIs(Stopping.class);
    }

    private void assertIsPlaying() {
        assertCurrentStateIs(Playing.class);
    }
}
