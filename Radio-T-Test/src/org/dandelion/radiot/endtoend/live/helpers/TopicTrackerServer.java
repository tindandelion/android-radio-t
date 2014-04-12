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

    public void changeTopic(String text, String link) throws IOException {
        HttpPost request = new HttpPost(serverUrl("set-topic"));
        request.setEntity(new StringEntity(text + "\n" + link));
        executeRequest(request);
    }

    private void checkResponse(HttpResponse response) {
        final int HTTP_OK = 200;
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() != HTTP_OK)
            throw new RuntimeException("Unable to change the topic: " + statusLine);
    }

    private String serverUrl(String command) {
        return format("http://%s/%s", baseUrl, command);
    }

    private void executeRequest(HttpPost request) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        checkResponse(client.execute(request));
    }

    public void heartbeat() throws IOException {
        HttpPost request = new HttpPost(serverUrl("heartbeat"));
        executeRequest(request);
    }

}
