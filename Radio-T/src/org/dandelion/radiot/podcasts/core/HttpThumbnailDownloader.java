package org.dandelion.radiot.podcasts.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;
import java.net.URL;

public class HttpThumbnailDownloader implements PodcastList.ThumbnailDownloader {
    @Override
    public Bitmap loadPodcastImage(PodcastItem item) {
        String url = fullyQualifiedUrl(item);
        return BitmapFactory.decodeStream(openImageStream(url));
    }

    private InputStream openImageStream(String url) {
        try {
            return new URL(url).openStream();
        } catch (Exception ex) {
            return null;
        }
    }

    private String fullyQualifiedUrl(PodcastItem item) {
        return ThumbnailUrl.construct(item.getThumbnailUrl());
    }

    public static class ThumbnailUrl {
        private static final String HOST = "http://www.radio-t.com";

        public static String construct(String urlPart) {
            if (null == urlPart) {
                return null;
            }
            return constructFullUrl(urlPart);
        }

        private static String constructFullUrl(String urlPart) {
            Uri uri = Uri.parse(urlPart);
            if (uri.isAbsolute()) {
                return urlPart;
            } else {
                return HOST + urlPart;
            }
        }
    }
}
