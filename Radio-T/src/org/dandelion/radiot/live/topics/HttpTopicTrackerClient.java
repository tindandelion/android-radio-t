package org.dandelion.radiot.live.topics;

import android.os.AsyncTask;
import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.live.ui.topics.TopicListener;
import org.dandelion.radiot.live.ui.topics.TopicTracker;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HttpTopicTrackerClient implements TopicTracker {
    private final HttpClient client;
    private TopicListener listener;
    private final String serverUrl;

    public HttpTopicTrackerClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.client = new ApacheHttpClient();
    }

    @Override
    public void setListener(TopicListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        requestTopic();
    }

    private void requestTopic() {
        new HttpTopicRequest(this, listener).execute();
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
        private final TopicListener listener;

        HttpTopicRequest(HttpTopicTrackerClient client, TopicListener listener) {
            this.client = client;
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Void... params) {
            return client.retrieveTopic();
        }

        @Override
        protected void onPostExecute(Object result) {
            listener.onTopicChanged((String) result);
        }
    }
}
