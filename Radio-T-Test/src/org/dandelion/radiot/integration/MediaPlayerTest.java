package org.dandelion.radiot.integration;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.helpers.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

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

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveStreamServer();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void tearDown() throws Exception {
        player.release();
        backend.stop();
        super.tearDown();
    }

    private static class LiveStreamServer extends NanoHTTPD {
        public static final int PORT = 32768;
        private static final String DIRECT_URL = "http://icecast.bigrradio.com/80s90s";
        private static final String REDIRECT_RESOURCE = "/stream";
        public static final String REDIRECT_URL =
                String.format("http://localhost:%d", PORT) + REDIRECT_RESOURCE;

        public LiveStreamServer() throws IOException {
            super(PORT, new File(""));
        }

        @Override
        public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
            if (uri.equals(REDIRECT_RESOURCE)) {
                return redirectToStream();
            }
            return null;
        }

        private Response redirectToStream() {
            Response response = new Response("302 Found", MIME_HTML, "");
            response.addHeader("Location", DIRECT_URL);
            return response;
        }
    }
}
