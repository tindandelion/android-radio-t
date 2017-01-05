package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.http.*;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpTopicProvider implements Provider<CurrentTopic> {
    private final HttpClient client;
    private final String serverUrl;

    static String topicRequestUrl(String serverUrl) {
        return serverUrl + "/api/chat/v1/topic";
    }

    HttpTopicProvider(HttpClient client, String serverUrl) {
        this.client = client;
        this.serverUrl = serverUrl;
    }

    public HttpTopicProvider(String serverUrl) {
        this(OkBasedHttpClient.make(), serverUrl);
    }

    private CurrentTopic parseResponseJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String id = obj.getString("id");
            String text = obj.getString("text");
            return CurrentTopic.create(id, text);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CurrentTopic get() throws Exception {
        try {
            String json = client.getStringContent(topicRequestUrl(serverUrl));
            return parseResponseJson(json);
        } catch (NoContentException ex) {
            return CurrentTopic.empty();
        }
    }

    @Override
    public void abort() {
        client.shutdown();
    }
}
