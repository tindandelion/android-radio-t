package org.dandelion.radiot.podcasts.ui;

import android.graphics.drawable.Drawable;
import org.dandelion.radiot.podcasts.core.PodcastItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PodcastVisual {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static SimpleDateFormat INPUT_DATE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public final PodcastItem podcast;
    public final String number;
    public final String pubDate;

    public Drawable thumbnail;

    public PodcastVisual(PodcastItem p, Drawable thumbnail) {
        this.podcast = p;
        this.number = extractNumberFrom(p.title);
        this.pubDate = extractPubDateFrom(p.pubDate);
        this.thumbnail = thumbnail;
    }

    private String extractPubDateFrom(String value) {
        try {
            Date date = INPUT_DATE_FORMAT.parse(value);
            return OUTPUT_DATE_FORMAT.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public void setThumbnail(Drawable value) {
        if (value != null) {
            thumbnail = value;
        }
    }

    private String extractNumberFrom(String value) {
        if (value == null) {
            return "";
        }

        Matcher matcher = NUMBER_PATTERN.matcher(value);
        if (matcher.find()) {
            return "#" + matcher.group();
        }
        return value;
    }
}
