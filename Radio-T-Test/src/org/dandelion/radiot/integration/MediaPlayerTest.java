package org.dandelion.radiot.integration;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.integration.helpers.LiveStreamServer;

public class MediaPlayerTest extends InstrumentationTestCase {
    private MediaPlayer player;
    private LiveStreamServer backend;

    public void testWhenGivenDirectUrl_PlaysIt() throws Exception {
        player.setDataSource(LiveStreamServer.DIRECT_URL);
        player.prepare();
        player.start();
        Thread.sleep(2000);
        assertTrue(player.isPlaying());
    }

    public void testWhenGivenRedirectUrl_FollowsRedirection() throws Exception {
        player.setDataSource(LiveStreamServer.REDIRECT_URL);
        player.prepare();
        player.start();
        Thread.sleep(2000);
        assertTrue(player.isPlaying());
    }

    public void testMultiplePlaybackTest() throws Exception {
        for(int i = 0; i < 3; i++) {
            MediaPlayer p = new MediaPlayer();
            p.setDataSource(LiveStreamServer.REDIRECT_URL);
            p.prepare();
            p.start();
            Thread.sleep(2000);
            p.release();
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveStreamServer(getInstrumentation().getContext());
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void tearDown() throws Exception {
        player.release();
        backend.stop();
        super.tearDown();
    }

}
