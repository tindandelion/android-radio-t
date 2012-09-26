package org.dandelion.radiot.podcasts.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class PodcastVisual {
    public final PodcastItem podcast;
    private Bitmap thumbnail;
    private Bitmap defaultThumbnail;

    public PodcastVisual(PodcastItem p, Bitmap defaultThumbnail) {
        this.podcast = p;
        this.defaultThumbnail = defaultThumbnail;
    }

    public Bitmap getThumbnail() {
        if (thumbnail == null) {
            thumbnail = decodeThumbnail();
        }
        return thumbnail;
    }

    private Bitmap decodeThumbnail() {
        Bitmap bitmap = null;
        byte[] thData = podcast.getThumbnailData();

        if (thData != null) {
            bitmap = BitmapFactory.decodeByteArray(thData, 0, thData.length);
        }

        if (bitmap != null) {
            return bitmap;
        } else {
            return defaultThumbnail;
        }
    }
}
