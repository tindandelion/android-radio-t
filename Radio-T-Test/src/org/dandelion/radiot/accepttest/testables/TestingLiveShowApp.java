package org.dandelion.radiot.accepttest.testables;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.LiveShowApp;
import org.dandelion.radiot.live.core.AudioStream;

import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestingLiveShowApp extends LiveShowApp {
    private boolean isPlaying = false;
    private AudioStream audioStream;
    private String audioUrl = "";

    public TestingLiveShowApp() {
        super();
    }

    @Override
    public AudioStream createAudioStream(MediaPlayer mediaPlayer) {
        audioStream = new AudioStream(mediaPlayer) {
            @Override
            public void play(String url) throws IOException {
                super.play(audioUrl);
                isPlaying = true;
            }

            @Override
            public void stop() {
                super.stop();
                isPlaying = false;
            }
        };
        return audioStream;
    }

    public void assertIsPlaying() {
        assertTrue(isPlaying);
    }

    public void assertIsStopped() {
        assertFalse(isPlaying);
    }

    public void reset() {
        if (audioStream != null) {
            audioStream.reset();
        }
    }

    public void setAudioUrl(String value) {
        this.audioUrl = value;
    }
}
