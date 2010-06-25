package org.dandelion.radiot;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;

public class PodcastItem implements Cloneable {
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
	private int number;
	private Date date;
	private String showNotes;
	private Uri audioUri;

	public PodcastItem(int number, Date issueDate, String showNotes,
			String audioLink) {
		this.number = number;
		this.date = issueDate;
		this.showNotes = showNotes;
		this.audioUri = Uri.parse(audioLink);
	}

	public PodcastItem(int number) {
		this(number, null, null, "");
	}

	public Uri getAudioUri() {
		return audioUri;
	}

	public PodcastItem() {
	}

	Date getDate() {
		return date;
	}

	public String getShowNotes() {
		return showNotes;
	}

	public int getNumber() {
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
		//TODO: Check errors while extracting number 
		Matcher matcher = NUMBER_PATTERN.matcher(value);
		matcher.find();
		number = Integer.parseInt(matcher.group());
	}
}
