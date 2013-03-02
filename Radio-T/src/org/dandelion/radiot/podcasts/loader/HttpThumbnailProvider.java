package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.HttpClient;

public class HttpThumbnailProvider {
    private HttpClient httpClient;

    public HttpThumbnailProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public byte[] thumbnailDataFor(String url) {
        try {
            return httpClient.getByteContent(url);
        } catch (Exception ex) {
            return null;
        }
    }
}
