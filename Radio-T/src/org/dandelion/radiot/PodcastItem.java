package org.dandelion.radiot;

import android.graphics.Bitmap;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PodcastItem implements Cloneable {
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
	private static SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	

	private String number;
	private String pubDate;
	private String showNotes;
	private Uri audioUri;
	private String thumbnailUrl;
	private Bitmap thumbnail;

	public Uri getAudioUri() {
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

	public String getNumber() {
		return number;
	}

	public PodcastItem copy() {
		try {
			return (PodcastItem) clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void extractPodcastNumber(String value) {
		Matcher matcher = NUMBER_PATTERN.matcher(value);
		if (matcher.find()) {
			number = "#" + matcher.group();
		} else {
			number = value;
		}

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

	public void extractAudioUri(String value) {
		audioUri = Uri.parse(value);
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Bitmap value) {
		thumbnail = value;
	}

    public void setThumbnailUrl(String value) {
        thumbnailUrl = value;
    }
}
