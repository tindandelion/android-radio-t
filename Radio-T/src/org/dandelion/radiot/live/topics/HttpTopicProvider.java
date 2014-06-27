package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.Provider;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HttpTopicProvider implements Provider<String> {
    private final HttpClient client;
    private final String serverUrl;

    public HttpTopicProvider(String serverUrl) {
        this.serverUrl = serverUrl;
        this.client = new ApacheHttpClient();
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
        try {
            String json = client.getStringContent(trackerServerUrl());
            return parseResponseJson(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void abort() {
        client.shutdown();
    }
}
