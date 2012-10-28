package org.dandelion.radiot.podcasts.core;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PodcastItem implements Cloneable, Serializable {
    public static final Pattern THUMBNAIL_URL_PATTERN = Pattern.compile("<img\\s+src=\"(\\S+)\".*/>");
    private static SimpleDateFormat INPUT_DATE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");


    private String pubDate;
	private String showNotes;
	private String audioUri;
    private String title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String value) {
        thumbnailUrl = value;
    }
}
