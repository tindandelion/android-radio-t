package org.dandelion.radiot.helpers;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AudioStreamServer extends NanoHTTPD {
    public static int PORT = 32768;
    public static String STREAM_RESOURCE = "/stream";
    private static final String MAIN_RESOURCE = "/main";
    public static String DIRECT_URL = String.format("http://localhost:%d", PORT) + STREAM_RESOURCE;
    public static String MAIN_URL = String.format("http://localhost:%d", PORT) + MAIN_RESOURCE;
    private Context context;

    public AudioStreamServer(Context context) throws IOException {
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
