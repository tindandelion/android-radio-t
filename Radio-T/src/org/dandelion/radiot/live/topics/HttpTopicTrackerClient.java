package org.dandelion.radiot.live.topics;

import android.os.AsyncTask;
import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.Consumer;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.live.ui.topics.TopicTracker;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HttpTopicTrackerClient implements TopicTracker {
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
        new HttpTopicRequest(this, consumer).execute();
    }

    private String retrieveTopic() {
        try {
            String json = client.getStringContent(trackerServerUrl());
            return parseResponseJson(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    static class HttpTopicRequest extends AsyncTask<Void, Void, Object> {
        private final HttpTopicTrackerClient client;
        private final Consumer<String> consumer;

        HttpTopicRequest(HttpTopicTrackerClient client, Consumer<String> consumer) {
            this.client = client;
            this.consumer = consumer;
        }

        @Override
        protected Object doInBackground(Void... params) {
            return client.retrieveTopic();
        }

        @Override
        protected void onPostExecute(Object result) {
            consumer.accept((String) result);
        }
    }
}
