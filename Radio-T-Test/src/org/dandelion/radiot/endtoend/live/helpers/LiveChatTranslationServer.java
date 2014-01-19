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
        hasReceivedRequest("/api/last/50", "");
    }

    public void hasReceivedContinuationRequest(int seq) throws InterruptedException {
        hasReceivedRequest("/api/new/" + seq, "");
    }

    public void respondWithError() {
        respondWith(new Response(HTTP_NOTFOUND, MIME_HTML, ""));
    }
}
