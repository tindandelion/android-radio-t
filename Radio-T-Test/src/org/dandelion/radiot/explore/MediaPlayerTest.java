package org.dandelion.radiot.explore;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.helpers.LiveStreamServer;

import static org.hamcrest.CoreMatchers.equalTo;

public class MediaPlayerTest extends InstrumentationTestCase {
    private MediaPlayer player;
    private LiveStreamServer backend;

    public void testWhenGivenDirectUrl_PlaysIt() throws Exception {
        player.setDataSource(LiveStreamServer.DIRECT_URL);
        player.prepare();
        backend.hasReceivedRequest(equalTo(LiveStreamServer.DIRECT_RESOURCE));

        player.start();
        playSomeTime();
        assertTrue(player.isPlaying());
    }


    public void testWhenGivenRedirectUrl_FollowsRedirection() throws Exception {
        player.setDataSource(LiveStreamServer.REDIRECT_URL);
        player.prepare();
        backend.hasReceivedRequest(equalTo(LiveStreamServer.REDIRECT_RESOURCE));
        backend.hasReceivedRequest(equalTo(LiveStreamServer.DIRECT_RESOURCE));

        player.start();
        playSomeTime();
        assertTrue(player.isPlaying());
    }

    public void testMultiplePlaybackTest() throws Exception {
        for(int i = 0; i < 3; i++) {
            MediaPlayer p = new MediaPlayer();
            p.setDataSource(LiveStreamServer.REDIRECT_URL);
            p.prepare();
            p.start();
            playSomeTime();
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

    private void playSomeTime() throws InterruptedException {
        Thread.sleep(2000);
    }
}
