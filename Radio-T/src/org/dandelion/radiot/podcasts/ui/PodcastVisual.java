package org.dandelion.radiot.podcasts.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class PodcastVisual {
    public final PodcastItem podcast;
    public Drawable thumbnail;
    private Resources resources;

    public PodcastVisual(PodcastItem p, Drawable thumbnail, Resources res) {
        this.podcast = p;
        this.thumbnail = thumbnail;
        this.resources = res;
    }

    public void setThumbnail(byte[] data) {
        Drawable newValue = createBitmapDrawable(data);
        if (newValue != null) {
            thumbnail = newValue;
        }
    }

    private Drawable createBitmapDrawable(byte[] thData) {
        if (thData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(thData, 0, thData.length);
            if (bitmap != null) {
                return new BitmapDrawable(resources, bitmap);
            }
        }
        return null;
    }
}
