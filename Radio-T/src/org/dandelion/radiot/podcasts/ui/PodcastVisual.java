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
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public final PodcastItem podcast;
    public final String number;
    public final String pubDate;
    public final String showNotes;

    public Drawable thumbnail;
    private PodcastItemView assocView;

    public PodcastVisual(PodcastItem p, Drawable thumbnail) {
        this.podcast = p;
        this.number = formatNumber(p.title);
        this.pubDate = formatPubDate(p.pubDate);
        this.showNotes = extractShowNotes(p.showNotes);
        this.thumbnail = thumbnail;
    }

    public void updateThumbnail(Drawable value) {
        if (value != null) {
            thumbnail = value;
            if (assocView != null) {
                assocView.setThumbnail(thumbnail);
            }
        }
    }

    private String extractShowNotes(String value) {
        return value.trim();
    }

    private String formatPubDate(String value) {
        try {
            Date date = INPUT_DATE_FORMAT.parse(value);
            return OUTPUT_DATE_FORMAT.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    private String formatNumber(String value) {
        if (value == null) {
            return "";
        }

        Matcher matcher = NUMBER_PATTERN.matcher(value);
        if (matcher.find()) {
            return "● " + matcher.group();
        }
        return value;
    }

    public void disassociate() {
        assocView = null;
    }

    public void associateWith(PodcastItemView row) {
        assocView = row;
        row.populateWith(this);
    }
}
