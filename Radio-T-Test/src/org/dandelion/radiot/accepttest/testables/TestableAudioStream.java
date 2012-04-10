package org.dandelion.radiot.accepttest.testables;

import android.util.Log;
import org.dandelion.radiot.live.core.AudioStream;

import java.io.IOException;

public class TestableAudioStream extends AudioStream {
    public String url = "";

    public TestableAudioStream() {
        super(null);
    }

    @Override
    public void play() throws IOException {
        playUrl(url);
    }

    @Override
    protected void playUrl(String url) throws IOException {
        Log.d("LIVE", "Playing URL: " + url);
        super.playUrl(url);
    }

    @Override
    public void release() {
        // Suppress releasing the media player
    }

    public void doRelease() {
        super.release();
    }
}
