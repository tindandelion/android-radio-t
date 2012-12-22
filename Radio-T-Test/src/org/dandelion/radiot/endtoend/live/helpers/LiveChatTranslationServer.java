package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.helpers.ResponsiveHttpServer;

import java.io.IOException;

public class LiveChatTranslationServer extends ResponsiveHttpServer {
    private static final String MIME_JSON = "application/json";

    public LiveChatTranslationServer() throws IOException {
        super();
    }

    public void respondWithChatStream(String content) {
        respondSuccessWith(content, MIME_JSON);
    }

    public void hasReceivedInitialRequest() throws InterruptedException {
        hasReceivedRequest("/data/jsonp", "mode=last&recs=10");
    }

    public void hasReceivedContinuationRequest() throws InterruptedException {
        hasReceivedRequest("/data/jsonp", "mode=next&recs=10");
    }
}
