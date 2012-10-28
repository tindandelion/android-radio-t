package org.dandelion.radiot.podcasts.core;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PodcastItem implements Cloneable, Serializable {
    public static final Pattern THUMBNAIL_URL_PATTERN = Pattern.compile("<img\\s+src=\"(\\S+)\".*/>");

    public String pubDate = "";
	private String showNotes = "";
	private String audioUri = "";
    public String title = "";
    private String thumbnailUrl = null;

    public String getAudioUri() {
		return audioUri;
	}

	public PodcastItem() {
	}

    public String getShowNotes() {
		return showNotes;
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
