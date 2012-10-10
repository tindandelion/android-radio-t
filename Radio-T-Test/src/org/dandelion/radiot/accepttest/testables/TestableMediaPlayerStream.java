package org.dandelion.radiot.accepttest.testables;

import android.media.MediaPlayer;
import org.dandelion.radiot.live.MediaPlayerStream;
import org.dandelion.radiot.live.util.ConstantProvider;

import java.io.IOException;

public class TestableMediaPlayerStream extends MediaPlayerStream {
    private static String ACTIVE_TRANSLATION_URL = "http://174.37.110.72:5020";
    private static String INACTIVE_TRANSLATION_URL = "http://non-existent";


    private String url = "";

    public TestableMediaPlayerStream() {
        super(new MediaPlayer(), new ConstantProvider<String>(""));
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
        url = ACTIVE_TRANSLATION_URL;
    }
}
