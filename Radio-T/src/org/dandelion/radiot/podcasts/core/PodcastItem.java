package org.dandelion.radiot.podcasts.core;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Remove thumbnailData from PodcastItem

public class PodcastItem implements Cloneable, Serializable {
    public static final Pattern THUMBNAIL_URL_PATTERN = Pattern.compile("<img\\s+src=\"(\\S+)\".*/>");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
	private static SimpleDateFormat INPUT_DATE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");


    private String numberString;
    private String pubDate;
	private String showNotes;
	private String audioUri;
    private String title;
    private byte[] thumbnailData;
    private String thumbnailUrl;

    public String getAudioUri() {
		return audioUri;
	}

	public PodcastItem() {
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getShowNotes() {
		return showNotes;
	}

	public String getNumberString() {
		return numberString;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
        String rawNumber = extractPodcastNumber(value);
        if (rawNumber != null) {
            numberString = "#" + rawNumber;
        } else {
            numberString = title;
        }
	}

    private String extractPodcastNumber(String value) {
        Matcher matcher = NUMBER_PATTERN.matcher(value);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public void extractPubDate(String value) {
		try {
			Date date = INPUT_DATE_FORMAT.parse(value);
			pubDate = OUTPUT_DATE_FORMAT.format(date);
		} catch (ParseException e) {
			pubDate = "";
		}
	}

	public void extractShowNotes(String value) {
		showNotes = value.trim();
	}

    public void setAudioUri(String value) {
        audioUri = value;
    }

    public void extractThumbnailUrl(String description) {
        Matcher matcher = THUMBNAIL_URL_PATTERN.matcher(description);
        if (matcher.find()) {
            setThumbnailUrl(matcher.group(1));
        }
    }

    public void setThumbnailData(byte[] data) {
        thumbnailData = data;
    }

    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String value) {
        thumbnailUrl = value;
    }
}
