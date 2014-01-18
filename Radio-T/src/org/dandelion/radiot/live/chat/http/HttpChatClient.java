package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.live.chat.Message;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class HttpChatClient {
    private static final int READ_TIMEOUT_MS = 20 * 1000;

    private final String baseUrl;
    private final HttpClient client;

    public HttpChatClient(String baseUrl) {
        this.baseUrl = baseUrl;
        client = new ApacheHttpClient();
        client.setReadTimeout(READ_TIMEOUT_MS);
    }

    public List<Message> retrieveMessages(String mode) throws IOException, JSONException {
        return parseMessages(requestMessages(mode));
    }

    private String requestMessages(String mode) throws IOException {
        return client.getStringContent(chatStreamUrl(mode));
    }

    private String chatStreamUrl(String mode) {
        if (mode.equals("last")) {
            return lastRecordsUrl();
        }
        return baseUrl + "/data/jsonp?mode=" + mode + "&recs=10";
    }

    private String lastRecordsUrl() {
        return baseUrl + "/api/last/50";
    }

    private List<Message> parseMessages(String json) throws JSONException {
        return HttpResponseParser.parse(json);
    }

    public void shutdown() {
        client.shutdown();
    }
}
