package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.Provider;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpTopicProvider implements Provider<String> {
    private final HttpClient client;
    private final String serverUrl;

    public HttpTopicProvider(HttpClient client, String serverUrl) {
        this.client = client;
        this.serverUrl = serverUrl;
    }

    public HttpTopicProvider(String serverUrl) {
        this(new ApacheHttpClient(), serverUrl);
    }

    private String parseResponseJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.getString("text");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String trackerServerUrl() {
        return serverUrl + "/chat/topic";
    }

    @Override
    public String get() throws Exception {
        String json = client.getStringContent(trackerServerUrl());
        return parseResponseJson(json);
    }

    @Override
    public void abort() {
        client.shutdown();
    }
}
