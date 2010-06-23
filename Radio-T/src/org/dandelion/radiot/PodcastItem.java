package org.dandelion.radiot;

import android.net.Uri;

public class PodcastItem {
	private int number;
	private String date;
	private String showNotes;
	private Uri audioUri;
	
	public PodcastItem(int number, String date, String showNotes,
			String audioLink) {
		this.number = number;
		this.date = date;
		this.showNotes = showNotes;
		this.audioUri = Uri.parse(audioLink);
	}
	
	public PodcastItem(int number, String date, String showNotes) {
		this(number, date, showNotes, "");
	}

	public PodcastItem(int number) {
		this(number, null, null, "");
	}

	public Uri getAudioUri() {
		return audioUri;
	}

	public PodcastItem() {
	}

	public String getDate() {
		return date;
	}

	public String getShowNotes() {
		return showNotes;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return Integer.toString(number);
	}

}
