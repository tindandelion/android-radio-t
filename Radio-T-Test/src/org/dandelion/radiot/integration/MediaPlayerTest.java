package org.dandelion.radiot.integration;

import android.content.Context;
import android.media.MediaPlayer;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.helpers.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MediaPlayerTest extends InstrumentationTestCase {
    private StreamServer backend;
    private MediaPlayer player;

    public void testConnection() throws Exception {
        player.setDataSource(StreamServer.REDIRECT_URL);
        player.prepare();
        player.start();
        Thread.sleep(3000);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new StreamServer(getInstrumentation().getContext());
        player = new MediaPlayer();
    }

    @Override
    public void tearDown() throws Exception {
        player.release();
        backend.stop();
        super.tearDown();
    }

    private static class StreamServer extends NanoHTTPD {
        public static int PORT = 32768;
        public static String STREAM_RESOURCE = "/stream";
        private static final String MAIN_RESOURCE = "/main";
        public static String DIRECT_URL = String.format("http://localhost:%d", PORT) + STREAM_RESOURCE;
        public static String REDIRECT_URL = String.format("http://localhost:%d", PORT) + MAIN_RESOURCE;
        private Context context;

        public StreamServer(Context context) throws IOException {
            super(PORT, new File(""));
            this.context = context;
        }

        @Override
        public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
            if (uri.equals(MAIN_RESOURCE)) {
                return redirectToStream();
            }
            if (uri.equals(STREAM_RESOURCE)) {
                return respondWithStream();
            }
            return null;
        }

        private Response redirectToStream() {
            Response response = new Response("302 Found", MIME_HTML, "");
            response.addHeader("Location", DIRECT_URL);
            return response;
        }

        private Response respondWithStream() {
            try {
                return new Response(HTTP_OK, "audio/mpeg", openAudioStream());
            } catch (IOException e) {
                return null;
            }
        }

        private InputStream openAudioStream() throws IOException {
            return context.getAssets().open("stream.mp3");
        }
    }
}
