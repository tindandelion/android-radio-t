package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.helpers.ResponsiveHttpServer;

import java.io.IOException;

public class LiveChatTranslationServer extends ResponsiveHttpServer {
    private static final String MIME_JSON = "application/json";
    public static final String SESSION_COOKIE = "JSESSIONID";
    public static final String SESSION_ID = "123445667";

    public LiveChatTranslationServer() throws IOException {
        super();
        setCookie(SESSION_COOKIE, SESSION_ID);
    }

    public void respondWithChatStream(String content) {
        respondSuccessWith(content, MIME_JSON);
    }

    public void hasReceivedInitialRequest() throws InterruptedException {
        hasReceivedRequest("/data/jsonp", "mode=last&recs=10");
    }

    public void hasReceivedContinuationRequest() throws InterruptedException {
        String[] expectedCookie = new String[]{SESSION_COOKIE, SESSION_ID};
        hasReceivedRequest("/data/jsonp", "mode=next&recs=10", expectedCookie);
    }

    public void respondWithError() {
        respondWith(new Response(HTTP_NOTFOUND, MIME_HTML, ""));
    }
}
