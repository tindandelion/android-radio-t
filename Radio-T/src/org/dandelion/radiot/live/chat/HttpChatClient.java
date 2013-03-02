package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
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
        return baseUrl + "/data/jsonp?mode=" + mode + "&recs=10";
    }

    private List<Message> parseMessages(String json) throws JSONException {
        return ResponseParser.parse(json);
    }

    public void shutdown() {
        client.shutdown();
    }
}
