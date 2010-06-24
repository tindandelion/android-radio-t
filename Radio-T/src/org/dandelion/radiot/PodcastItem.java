package org.dandelion.radiot;

import java.util.Date;

import android.net.Uri;

public class PodcastItem {
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
}
