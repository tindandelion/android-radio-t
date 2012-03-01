package org.dandelion.radiot.live;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.core.AudioStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AudioStreamTests {
    private static final String TEST_STREAM_URL = "http://audio-stream";
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer player = mock(MediaPlayer.class);
    private AudioStream stream;
    private AudioStream.StateListener stateListener;

    @Before
    public void setUp() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                preparedListener = (MediaPlayer.OnPreparedListener) invocation.getArguments()[0];
                return null;
            }
        }).when(player).setOnPreparedListener(any(MediaPlayer.OnPreparedListener.class));

        stream = new AudioStream(player, TEST_STREAM_URL);
        stateListener = mock(AudioStream.StateListener.class);
        stream.setStateListener(stateListener);
    }

    @Test
    public void startPlayingPreparesPlayer() throws IOException {
        stream.play();
        verify(player).setDataSource(TEST_STREAM_URL);
        verify(player).prepareAsync();
    }

    @Test
    public void resetsPlayerBeforePreparing() throws Exception {
        stream.play();
        verify(player).reset();
    }

    @Test
    public void startsPlayingWhenPrepared() throws Exception {
        stream.play();
        preparedListener.onPrepared(player);
        verify(player).start();
    }

    @Test
    public void stopPlaying() throws Exception {
        stream.stop();
        verify(player).stop();
    }

    @Test
    public void startPlayingCallback() throws Exception {
        stream.play();
        preparedListener.onPrepared(player);

        verify(stateListener).onStarted();
    }

    @Test
    public void stopPlayingCallback() throws Exception {
        stream.stop();
        verify(stateListener).onStopped();
    }

    //TODO: Error callback
}
