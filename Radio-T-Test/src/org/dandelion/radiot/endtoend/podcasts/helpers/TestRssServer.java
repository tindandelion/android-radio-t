package org.dandelion.radiot.endtoend.podcasts.helpers;

import org.dandelion.radiot.helpers.HttpServer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestRssServer extends HttpServer {
    private BlockingQueue<Response> responseHolder = new LinkedBlockingDeque<Response>();

    public TestRssServer() throws IOException {
        super();
    }

    @Override
    protected Response serveUri(String uri) {
        try {
            return responseHolder.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void respondSuccessWith(String body) {
        responseHolder.add(new Response(HTTP_OK, MIME_XML, body));
    }

    public void respondNotFoundError() {
        responseHolder.add(new Response(HTTP_NOTFOUND, MIME_HTML, ""));
    }

    @Override
    public void stop() {
        respondSuccessWith("");
        super.stop();
    }

    public void hasReceivedRequestForRss() throws InterruptedException {
        hasReceivedRequest(equalTo("/rss"));
    }

    public void hasReceivedRequestForUrl(String url) throws InterruptedException {
        hasReceivedRequest(equalTo(url));
    }

    public void hasNotReceivedAnyRequest() throws InterruptedException {
        hasNotReceivedRequest(any(String.class));
    }
}
