package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;
import java.net.URL;

public class HttpThumbnailProvider implements ThumbnailProvider {
    private static final String DEFAULT_HOST = "http://www.radio-t.com";
    private String defaultHost;

    public HttpThumbnailProvider() {
        this(DEFAULT_HOST);
    }

    public HttpThumbnailProvider(String host) {
        this.defaultHost = host;
    }

    @Override
    public Bitmap thumbnailFor(PodcastItem item) {
        return loadThumbnail(item.getThumbnailUrl());
    }

    private Bitmap loadThumbnail(String url) {
        if (null != url) {
            return BitmapFactory.decodeStream(openImageStream(constructFullUrl(url)));
        } else {
            return null;
        }
    }

    private InputStream openImageStream(String url) {
        try {
            return new URL(url).openStream();
        } catch (Exception ex) {
            return null;
        }
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
