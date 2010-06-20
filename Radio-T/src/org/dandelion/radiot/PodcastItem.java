package org.dandelion.radiot;

import android.net.Uri;

public class PodcastItem {
	private String number;
	private String date;
	private String showNotes;
	private Uri audioUri;

	public PodcastItem(String number, String date, String showNotes,
			String audioLink) {
		this.number = number;
		this.date = date;
		this.showNotes = showNotes;
		this.audioUri = Uri.parse(audioLink);
	}

	public Uri getAudioUri() {
		return audioUri;
	}

	public PodcastItem(String number, String date, String showNotes) {
		this(number, date, showNotes, "");
	}

	public PodcastItem(String number) {
		this(number, null, null);
	}

	public String getDate() {
		return date;
	}

	public String getShowNotes() {
		return showNotes;
	}

	public String getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return number;
	}
}
