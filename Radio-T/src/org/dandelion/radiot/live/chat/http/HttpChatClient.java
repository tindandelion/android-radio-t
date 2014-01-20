package org.dandelion.radiot.live.chat.http;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.live.chat.Message;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

// TODO: Collapse retrieveLastMessages() and retrieveNewMessages() into a single call?
public class HttpChatClient {
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

    public HttpChatClient(String baseUrl) {
        this.baseUrl = baseUrl;
        client = new ApacheHttpClient();
        client.setReadTimeout(READ_TIMEOUT_MS);
    }

    public HttpChatClient(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        client = httpClient;
    }

    public List<Message> retrieveLastMessages() throws IOException, JSONException {
        return parseMessages(requestLastMessages());
    }

    public List<Message> retrieveNewMessages() throws IOException, JSONException {
        return parseMessages(requestNewMessages());
    }

    private String requestNewMessages() throws IOException {
        String url = newRecordsUrl(baseUrl, lastMessageSeq);
        return client.getStringContent(url);
    }

    private String requestLastMessages() throws IOException {
        String url = lastRecordsUrl(baseUrl);
        return client.getStringContent(url);
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

    public void shutdown() {
        client.shutdown();
    }


    public int lastMessageSeq() {
        return lastMessageSeq;
    }
}
