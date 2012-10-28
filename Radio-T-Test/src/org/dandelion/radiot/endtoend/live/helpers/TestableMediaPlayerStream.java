package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.live.MediaPlayerStream;

public class TestableMediaPlayerStream extends MediaPlayerStream {

    public TestableMediaPlayerStream(String url) {
        super(url);
    }

    @Override
    public void release() {
        // Suppress releasing the media player
    }

    public void doRelease() {
        super.release();
    }

}
