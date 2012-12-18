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
}
