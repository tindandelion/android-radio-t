package org.dandelion.radiot.podcasts.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class PodcastVisual {
    public final PodcastItem podcast;
    private Bitmap thumbnail;

    public PodcastVisual(PodcastItem p) {
        podcast = p;
    }

    public Bitmap getThumbnail(Bitmap defaultValue) {
        if (thumbnail == null) {
            thumbnail = decodeThumbnail(defaultValue);
        }
        return thumbnail;
    }

    private Bitmap decodeThumbnail(Bitmap defaultValue) {
        Bitmap bitmap = null;
        byte[] thData = podcast.getThumbnailData();

        if (thData != null) {
            bitmap = BitmapFactory.decodeByteArray(thData, 0, thData.length);
        }

        if (bitmap != null) {
            return bitmap;
        } else {
            return defaultValue;
        }
    }
}
