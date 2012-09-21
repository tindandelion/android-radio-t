package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;
import java.net.URL;

public class HttpThumbnailProvider implements ThumbnailProvider {
    private static final String HOST = "http://www.radio-t.com";

    @Override
    public Bitmap loadPodcastImage(String url) {
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
            return HOST + url;
        }
    }
}
