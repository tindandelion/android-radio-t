package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.OkBasedHttpClient;
import org.dandelion.radiot.http.Provider;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class HttpChatClient implements Provider<List<Message>> {
    private static final int READ_TIMEOUT_MS = 20 * 1000;

    private final String baseUrl;
    private final HttpClient client;
    private int lastMessageSeq = 0;

    public static String lastRecordsUrl(String baseUrl) {
        return baseUrl + "/api/last/50";
    }

    public static String newRecordsUrl(String baseUrl, int lastMessageSeq) {
        return baseUrl + "/api/new/" + lastMessageSeq;
    }

    public static HttpChatClient create(String baseUrl) {
        HttpClient http = OkBasedHttpClient.make();
        http.setReadTimeout(READ_TIMEOUT_MS);
        return new HttpChatClient(baseUrl, http);
    }

    public HttpChatClient(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        client = httpClient;
    }

    @Override
    public List<Message> get() throws IOException, JSONException {
        return parseMessages(requestMessages());
    }

    private String requestMessages() throws IOException {
        String url;

        if (isFirstTime()) {
            url = lastRecordsUrl(baseUrl);
        } else {
            url = newRecordsUrl(baseUrl, lastMessageSeq);
        }
        return client.getStringContent(url);
    }

    private boolean isFirstTime() {
        return lastMessageSeq == 0;
    }

    private List<Message> parseMessages(String json) throws JSONException {
        List<Message> messages = HttpResponseParser.parse(json);
        updateLastMessageSeq(messages);
        return messages;
    }

    private void updateLastMessageSeq(List<Message> messages) {
        if (messages.isEmpty()) return;

        Message lastMessage = messages.get(messages.size() - 1);
        lastMessageSeq = lastMessage.seq;
    }

    @Override
    public void abort() {
        client.shutdown();
    }
}
