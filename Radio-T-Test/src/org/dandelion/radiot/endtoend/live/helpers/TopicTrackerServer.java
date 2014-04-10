package org.dandelion.radiot.endtoend.live.helpers;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import static java.lang.String.format;

public class TopicTrackerServer {
    private final String baseUrl;

    public TopicTrackerServer(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void changeTopic(String newTopic) throws IOException {
        broadcast(newTopic);
    }

    private void checkResponse(HttpResponse response) {
        final int HTTP_OK = 200;
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() != HTTP_OK)
            throw new RuntimeException("Unable to change the topic: " + statusLine);
    }

    private String changeTopicUrl() {
        return format("http://%s/set-topic", baseUrl);
    }

    public void broadcast(String value) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(changeTopicUrl());
        request.setEntity(new StringEntity(value));
        checkResponse(client.execute(request));
    }
}
