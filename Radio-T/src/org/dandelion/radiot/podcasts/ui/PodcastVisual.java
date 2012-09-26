package org.dandelion.radiot.podcasts.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class PodcastVisual {
    public final PodcastItem podcast;
    private Drawable thumbnail;
    private Resources resources;
    private Drawable defaultThumbnail;

    public PodcastVisual(PodcastItem p, Drawable defaultThumbnail, Resources res) {
        this.podcast = p;
        this.defaultThumbnail = defaultThumbnail;
        this.resources = res;
    }

    public Drawable getThumbnail() {
        if (thumbnail == null) {
            thumbnail = decodeThumbnail();
        }
        return thumbnail;
    }

    private Drawable decodeThumbnail() {
        Bitmap bitmap = null;
        byte[] thData = podcast.getThumbnailData();

        if (thData != null) {
            bitmap = BitmapFactory.decodeByteArray(thData, 0, thData.length);
        }

        if (bitmap != null) {
            return new BitmapDrawable(resources, bitmap);
        } else {
            return defaultThumbnail;
        }
    }

}
