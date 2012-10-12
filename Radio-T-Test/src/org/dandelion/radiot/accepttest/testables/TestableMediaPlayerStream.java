package org.dandelion.radiot.accepttest.testables;

import android.util.Log;
import org.dandelion.radiot.live.MediaPlayerStream;

import java.io.IOException;

public class TestableMediaPlayerStream extends MediaPlayerStream {
    private static String ACTIVE_TRANSLATION_URL = "http://174.37.110.72:5020";
    private static String INACTIVE_TRANSLATION_URL = "http://non-existent";

    private String url = "";

    public TestableMediaPlayerStream() {
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

    public void suppressTranslation() {
        url = INACTIVE_TRANSLATION_URL;
    }

    public void activateTranslation() {
        url = ACTIVE_TRANSLATION_URL;
    }
}
