package org.dandelion.radiot.endtoend.live.helpers;

import org.dandelion.radiot.helpers.ResponsiveHttpServer;
import org.json.JSONObject;

import java.io.IOException;

public class LiveChatTranslationServer extends ResponsiveHttpServer {
    private static final String MIME_JSON = "application/json";

    public LiveChatTranslationServer() throws IOException {
        super();
    }

    public void respondWithChatStream(JSONObject stream) {
        respondSuccessWith(wrapIntoCallback(stream), MIME_JSON);
    }

    private String wrapIntoCallback(JSONObject stream) {
        return String.format("callback_fn(%s);", stream);
    }
}
