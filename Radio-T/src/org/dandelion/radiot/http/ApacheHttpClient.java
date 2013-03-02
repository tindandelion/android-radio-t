package org.dandelion.radiot.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheHttpClient implements HttpClient {
    private static final int HTTP_OK = 200;
    private final DefaultHttpClient client = new DefaultHttpClient();

    @Override
    public String getStringContent(String url) throws IOException {
        HttpResponse response = client.execute(new HttpGet(url));
        checkSuccess(response);
        return EntityUtils.toString(response.getEntity());
    }

    public byte[] getByteContent(String fullUrl) throws IOException {
        HttpResponse response = client.execute(new HttpGet(fullUrl));
        checkSuccess(response);
        return EntityUtils.toByteArray(response.getEntity());
    }

    private boolean isSuccessful(HttpResponse response) {
        return (response.getStatusLine().getStatusCode() == HTTP_OK) &&
                (response.getEntity() != null);
    }

    private void checkSuccess(HttpResponse response) throws IOException {
        if (isSuccessful(response)) {
            return;
        }
        throw new IOException();
    }

}
