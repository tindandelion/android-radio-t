package org.dandelion.radiot.endtoend.podcasts.helpers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestRssServer extends NanoHTTPD {
    private CountDownLatch requestFlag = new CountDownLatch(1);
    private BlockingQueue<Response> responseHolder = new LinkedBlockingDeque<Response>();

    public TestRssServer() throws IOException {
        super(8080, new File(""));
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        requestFlag.countDown();
        try {
            return responseHolder.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void hasReceivedRequest() throws InterruptedException {
        assertThat(requestFlag, signalledWithinSeconds(10));
        requestFlag = new CountDownLatch(1);
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

    private Matcher<? super CountDownLatch> signalledWithinSeconds(final int seconds) {
        return new TypeSafeMatcher<CountDownLatch>() {
            @Override
            protected boolean matchesSafely(CountDownLatch countDownLatch) {
                try {
                    return countDownLatch.await(seconds, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("wait for condition until %d seconds", seconds));
            }
        };
    }

}
