package org.dandelion.radiot.integration;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.helpers.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public void testStressTest() throws Exception {
        for(int i = 0; i < 10; i++) {
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

    private static class LiveStreamServer extends NanoHTTPD {
        public static final int PORT = 32768;
        private static final String REDIRECT_RESOURCE = "/stream";
        private static final String DIRECT_RESOURCE = "/audio";
        public static final String REDIRECT_URL =
                String.format("http://localhost:%d", PORT) + REDIRECT_RESOURCE;
        private static final String DIRECT_URL =
                String.format("http://localhost:%d", PORT) + DIRECT_RESOURCE;
        private Context context;

        public LiveStreamServer(Context context) throws IOException {
            super(PORT, new File(""));
            this.context = context;
        }

        @Override
        public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
            if (uri.equals(REDIRECT_RESOURCE)) {
                return redirectToStream();
            }
            if (uri.equals(DIRECT_RESOURCE)) {
                return broadcastStream();
            }
            return null;
        }

        private Response broadcastStream() {
            try {
                return new Response(HTTP_OK, "audio/mpeg", openAudioStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private InputStream openAudioStream() throws IOException {
            return context.getAssets().open("stream.mp3");
        }

        private Response redirectToStream() {
            Response response = new Response("302 Found", MIME_HTML, "");
            response.addHeader("Location", DIRECT_URL);
            return response;
        }
    }
}
