package org.dandelion.radiot.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheHttpClient implements HttpClient {
    private final DefaultHttpClient client = new DefaultHttpClient();

    @Override
    public String getStringContent(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity());
    }
}
