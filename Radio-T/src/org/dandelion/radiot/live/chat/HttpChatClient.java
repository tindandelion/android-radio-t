package org.dandelion.radiot.live.chat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class HttpChatClient {
    private static final int READ_TIMEOUT_MS = 20 * 1000;

    private final String baseUrl;
    private final DefaultHttpClient httpClient;

    public HttpChatClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = createHttpClient();
    }

    private DefaultHttpClient createHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT_MS);
        return client;
    }

    public List<Message> retrieveMessages(String mode) throws IOException, JSONException {
        return parseMessages(requestMessages(mode));
    }

    private String requestMessages(String mode) throws IOException {
        HttpResponse response = httpClient.execute(new HttpGet(chatStreamUrl(mode)));
        return EntityUtils.toString(response.getEntity());
    }

    private String chatStreamUrl(String mode) {
        return baseUrl + "/data/jsonp?mode=" + mode + "&recs=10";
    }

    private List<Message> parseMessages(String json) throws JSONException {
        return ResponseParser.parse(json);
    }

    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }
}
