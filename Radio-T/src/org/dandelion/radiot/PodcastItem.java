package org.dandelion.radiot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dandelion.radiot.rss.RssEnclosure;
import org.dandelion.radiot.rss.RssItem;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;

public class PodcastItem implements Cloneable {
	private static final Pattern IMAGE_URL_PATTERN = Pattern.compile("<img.+src=\"(\\S+)\".*/>");
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
	private static SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
	private static SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	

	private String number;
	private String pubDate;
	private String showNotes;
	private Uri audioUri;
	private ArrayList<String> tags = new ArrayList<String>();
	private String imageUrl;
	private Bitmap image;

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

	private void extractPodcastNumber(String value) {
		Matcher matcher = NUMBER_PATTERN.matcher(value);
		if (matcher.find()) {
			number = "#" + matcher.group();
		} else {
			number = value;
		}

	}

	private void extractPubDate(String value) {
		try {
			Date date = INPUT_DATE_FORMAT.parse(value);
			pubDate = OUTPUT_DATE_FORMAT.format(date);
		} catch (ParseException e) {
			pubDate = "";
		}
	}

	private void extractShowNotes(String value) {
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

	private void extractImageUrl(String encoded) {
		Matcher matcher = IMAGE_URL_PATTERN.matcher(encoded);
		if (matcher.find()) {
			imageUrl = matcher.group(1);
		}
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap value) {
		image = value;
	}

	public static PodcastItem fromRss(RssItem rssItem) {
		PodcastItem item = new PodcastItem();
		item.extractPodcastNumber(rssItem.title);
		item.extractPubDate(rssItem.pubDate);
		item.extractShowNotes(rssItem.description);
		item.extractImageUrl(rssItem.encodedContent);
		for (String category : rssItem.categories) {
			item.addTag(category);
		}
		for (RssEnclosure enclosure : rssItem.getEnclosures("audio/mpeg")) {
			item.extractAudioUri(enclosure.url);
		}
		return item;
	}
}
