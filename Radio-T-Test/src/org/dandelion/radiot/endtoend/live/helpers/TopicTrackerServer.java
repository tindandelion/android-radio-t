package org.dandelion.radiot.endtoend.live.helpers;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import static java.lang.String.format;

public class TopicTrackerServer {
    private final String address;

    public TopicTrackerServer(String address) {
        this.address = address;
    }

    public void changeTopic(String newTopic) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(changeTopicUrl());
        request.setEntity(new StringEntity(newTopic));
        checkResponse(client.execute(request));
    }

    private void checkResponse(HttpResponse response) {
        final int HTTP_OK = 200;
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() != HTTP_OK)
            throw new RuntimeException("Unable to change the topic: " + statusLine);
    }

    private String changeTopicUrl() {
        return format("http://%s:8080/chat/set-topic", address);
    }
}
