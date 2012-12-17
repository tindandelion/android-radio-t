package org.dandelion.radiot.helpers;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class ResponsiveHttpServer extends HttpServer {
    private BlockingQueue<Response> responseHolder = new LinkedBlockingDeque<Response>();

    public ResponsiveHttpServer() throws IOException {
    }

    @Override
    protected Response serveUri(String uri) {
        try {
            return responseHolder.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void respondWith(Response response) {
        responseHolder.add(response);
    }

    public void respondSuccessWith(String body, String mimeType) {
        respondWith(new Response(HTTP_OK, mimeType, body));
    }

    @Override
    public void stop() {
        respondSuccessWith("", MIME_HTML);
        super.stop();
    }
}
