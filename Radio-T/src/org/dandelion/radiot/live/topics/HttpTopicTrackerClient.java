package org.dandelion.radiot.live.topics;

import org.dandelion.radiot.http.*;
import org.dandelion.radiot.live.ui.topics.TopicTracker;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HttpTopicTrackerClient implements TopicTracker, Provider<String> {
    private final HttpClient client;
    private Consumer<String> consumer;
    private final String serverUrl;

    public HttpTopicTrackerClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.client = new ApacheHttpClient();
    }

    @Override
    public void setConsumer(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void start() {
        requestTopic();
    }

    private void requestTopic() {
        HttpRequest.ErrorListener errorListener = new HttpRequest.ErrorListener() {
            @Override
            public void onError() {

            }
        };
        new HttpRequest<>(this, consumer, errorListener).execute();
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

    public void refreshTopic() {
        requestTopic();
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
}
