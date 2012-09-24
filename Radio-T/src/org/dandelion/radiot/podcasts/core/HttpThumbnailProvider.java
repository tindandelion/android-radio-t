package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpThumbnailProvider implements ThumbnailProvider {
    private static final String DEFAULT_HOST = "http://www.radio-t.com";
    public static final int HTTP_OK = 200;
    private String defaultHost;

    public HttpThumbnailProvider() {
        this(DEFAULT_HOST);
    }

    public HttpThumbnailProvider(String host) {
        this.defaultHost = host;
    }

    @Override
    public Bitmap thumbnailFor(PodcastItem item) {
        byte[] data = thumbnailDataFor(item);
        if (data != null) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } else {
            return null;
        }
    }

    @Override
    public byte[] thumbnailDataFor(PodcastItem item) {
        String url = item.getThumbnailUrl();
        if (url == null) {
            return null;
        }
        return thumbnailDataFor(constructFullUrl(url));
    }

    private byte[] thumbnailDataFor(String fullUrl) {
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

    private String constructFullUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri.isAbsolute()) {
            return url;
        } else {
            return defaultHost + url;
        }
    }
}
