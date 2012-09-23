package org.dandelion.radiot.endtoend.podcasts.helpers;

import org.dandelion.radiot.TestPodcastListLoader;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestRssServer extends NanoHTTPD {
    private static final long REQUEST_TIMEOUT = 10;
    private BlockingQueue<String> requestQueue = new LinkedBlockingQueue<String>();
    private BlockingQueue<Response> responseHolder = new LinkedBlockingDeque<Response>();

    public TestRssServer() throws IOException {
        super(TestPodcastListLoader.PORT, new File(""));
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        requestQueue.add(uri);
        try {
            return responseHolder.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void hasReceivedRequestForRss() throws InterruptedException {
        assertThat(requestQueue, receivedRequest("/rss"));
    }

    public void hasReceivedRequestForUrl(String request) {
        assertThat(requestQueue, receivedRequest(request));
    }

    private Matcher<? super BlockingQueue<String>> receivedRequest(final String expected) {
        return new TypeSafeMatcher<BlockingQueue<String>>() {
            @Override
            protected boolean matchesSafely(BlockingQueue<String> queue) {
                try {
                    String value = queue.poll(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                    return expected.equals(value);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("A request for URL ").appendValue(expected);
            }
        };
    }


    public void respondSuccessWith(String response) {
        responseHolder.add(new Response(HTTP_OK, MIME_XML, response));
    }

    public void respondNotFoundError() {
        responseHolder.add(new Response(HTTP_NOTFOUND, MIME_HTML, ""));
    }

    @Override
    public void stop() {
        respondSuccessWith("");
        super.stop();
    }

}
