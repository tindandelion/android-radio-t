package org.dandelion.radiot.integration;

import android.media.MediaPlayer;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.helpers.AudioStreamServer;

public class MediaPlayerTest extends InstrumentationTestCase {
    private AudioStreamServer backend;
    private MediaPlayer player;

    public void testConnection() throws Exception {
        player.setDataSource(AudioStreamServer.MAIN_URL);
        player.prepare();
        player.start();
        Thread.sleep(3000);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new AudioStreamServer(getInstrumentation().getContext());
        player = new MediaPlayer();
    }

    @Override
    public void tearDown() throws Exception {
        player.release();
        backend.stop();
        super.tearDown();
    }

}
