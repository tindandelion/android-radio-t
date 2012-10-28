package org.dandelion.radiot.podcasts.ui;

import android.graphics.drawable.Drawable;
import org.dandelion.radiot.podcasts.core.PodcastItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PodcastVisual {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    public final PodcastItem podcast;
    public final String number;
    public Drawable thumbnail;

    public PodcastVisual(PodcastItem p, Drawable thumbnail) {
        this.podcast = p;
        this.number = extractNumberFrom(p.getTitle());
        this.thumbnail = thumbnail;
    }

    public void setThumbnail(Drawable value) {
        if (value != null) {
            thumbnail = value;
        }
    }

    private String extractNumberFrom(String value) {
        Matcher matcher = NUMBER_PATTERN.matcher(value);
        if (matcher.find()) {
            return "#" + matcher.group();
        }
        return value;
    }
}
