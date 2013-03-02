package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;

public class HttpThumbnailProvider implements ThumbnailProvider {
    private final HttpClient httpClient = new ApacheHttpClient();

    @Override
    public byte[] thumbnailDataFor(String url) {
        try {
            return httpClient.getByteContent(url);
        } catch (Exception ex) {
            return null;
        }
    }
}
