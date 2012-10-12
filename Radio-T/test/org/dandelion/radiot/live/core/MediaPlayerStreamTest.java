package org.dandelion.radiot.live.core;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.MediaPlayerStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MediaPlayerStreamTest {
    private static final String TEST_STREAM_URL = "http://audio-stream";
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer player = mock(MediaPlayer.class);
    private MediaPlayerStream stream;
    private AudioStream.Listener stateListener = mock(AudioStream.Listener.class);
    private MediaPlayer.OnErrorListener errorListener;
    private MediaPlayer.OnCompletionListener completionListener;

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

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                completionListener = (MediaPlayer.OnCompletionListener) invocation.getArguments()[0];
                return null;
            }
        }).when(player).setOnCompletionListener(any(MediaPlayer.OnCompletionListener.class));

        stream = new MediaPlayerStream(player, TEST_STREAM_URL);
        stream.setStateListener(stateListener);
    }

    @Test
    public void initializePlayerWhenStartPlaying() throws IOException {
        stream.play();
        verify(player).reset();
        verify(player).setDataSource(TEST_STREAM_URL);
        verify(player).prepareAsync();
    }

    @Test
    public void startsPlaybackWhenPrepared() throws Exception {
        stream.play();
        preparedListener.onPrepared(player);
        verify(player).start();
    }

    @Test
    public void releasesPlayerOnRelease() throws Exception {
        stream.release();
        verify(player).release();
    }

    @Test
    public void informsListenerThatStartedPlaying() throws Exception {
        preparedListener.onPrepared(player);
        verify(stateListener).onStarted();
    }

    @Test
    public void informsListenerOfPlaybackErrors() throws Exception {
        errorListener.onError(player, 0, 0);
        verify(stateListener).onError();
    }

    @Test
    public void treatsPlaybackCompletionEventAsError() throws Exception {
        completionListener.onCompletion(player);
        verify(stateListener).onError();
    }
}
