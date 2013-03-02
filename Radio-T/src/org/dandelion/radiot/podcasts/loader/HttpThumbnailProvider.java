package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.ApacheHttpClient;

public class HttpThumbnailProvider implements ThumbnailProvider {
    private final ApacheHttpClient httpClient = new ApacheHttpClient();

    @Override
    public byte[] thumbnailDataFor(String url) {
        try {
            return httpClient.getByteContent(url);
        } catch (Exception ex) {
            return null;
        }
    }
}
