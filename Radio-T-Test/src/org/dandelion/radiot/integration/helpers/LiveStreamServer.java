package org.dandelion.radiot.integration.helpers;

import android.content.Context;
import org.dandelion.radiot.helpers.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LiveStreamServer extends HttpServer {
    private static final String REDIRECT_RESOURCE = "/stream";
    private static final String DIRECT_RESOURCE = "/audio";
    public static final String REDIRECT_URL = HttpServer.addressForUrl(REDIRECT_RESOURCE);
    public static final String DIRECT_URL = HttpServer.addressForUrl(DIRECT_RESOURCE);
    private Context context;

    public LiveStreamServer(Context context) throws IOException {
        super();
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
