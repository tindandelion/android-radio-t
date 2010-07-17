package org.dandelion.radiot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.text.Html;

public class PodcastItem implements Cloneable {
	private static final Pattern IMAGE_URL_PATTERN = Pattern.compile("<img.+src=\"(.*)\".*/>");
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
	private static SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	

	private String number;
	private String pubDate;
	private String showNotes;
	private Uri audioUri;
	private ArrayList<String> tags = new ArrayList<String>();
	private Uri imageUri;

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
		String noHtml = Html.fromHtml(value).toString();
		showNotes = noHtml.replaceAll("\n", " ");
	}

	public void extractAudioUri(String value) {
		audioUri = Uri.parse(value);
	}

	public String getTagString() {
		if (tags.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(tags.get(0));
		for (int i = 1; i < tags.size(); i++) {
			builder
				.append(", ")
				.append(tags.get(i));
		}
		return builder.toString();
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public void extractImageUrl(String encoded) {
		Matcher matcher = IMAGE_URL_PATTERN.matcher(encoded);
		if (matcher.find()) {
			imageUri = Uri.parse(matcher.group(1));
		}
	}
	
	public Uri getImageUri() {
		return imageUri;
	}
}
