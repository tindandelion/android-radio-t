package org.dandelion.radiot.accepttest.testables;

import org.dandelion.radiot.live.MediaPlayerStream;

import java.io.IOException;

public class TestableMediaPlayerStream extends MediaPlayerStream {
    private static String INACTIVE_TRANSLATION_URL = "http://non-existent";

    private String url = "";
    private String validUrl;

    public TestableMediaPlayerStream(String validUrl) {
        super(null);
        this.validUrl = validUrl;
    }

    @Override
    public void play() throws IOException {
        playUrl(url);
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
        url = validUrl;
    }
}
