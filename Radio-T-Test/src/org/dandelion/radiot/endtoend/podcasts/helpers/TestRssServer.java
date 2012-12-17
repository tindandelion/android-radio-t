package org.dandelion.radiot.endtoend.podcasts.helpers;

import org.dandelion.radiot.helpers.ResponsiveHttpServer;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestRssServer extends ResponsiveHttpServer {

    public TestRssServer() throws IOException {
        super();
    }

    public void respondSuccessWith(String body) {
        respondSuccessWith(body, MIME_XML);
    }

    public void respondNotFoundError() {
        respondWith(new Response(HTTP_NOTFOUND, MIME_HTML, ""));
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
