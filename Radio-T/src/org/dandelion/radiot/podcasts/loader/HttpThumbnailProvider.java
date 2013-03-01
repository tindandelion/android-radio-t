package org.dandelion.radiot.podcasts.loader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpThumbnailProvider implements ThumbnailProvider {
    public static final int HTTP_OK = 200;

    @Override
    public byte[] thumbnailDataFor(String url) {
        return retrieveDataFrom(url);
    }


    private byte[] retrieveDataFrom(String fullUrl) {
        try {
            HttpResponse response = executeRequstFor(fullUrl);
            if (isSuccessful(response)) {
                return EntityUtils.toByteArray(response.getEntity());
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    private boolean isSuccessful(HttpResponse response) {
        return (response.getStatusLine().getStatusCode() == HTTP_OK) &&
                (response.getEntity() != null);
    }

    private HttpResponse executeRequstFor(String fullUrl) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        return client.execute(new HttpGet(fullUrl));
    }

}
