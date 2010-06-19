package org.dandelion.radiot;

public class PodcastItem {
	private String number;
	private String date;
	private String showNotes;

	public PodcastItem(String number) {
		this.number = number;
	}
	
	public PodcastItem(String number, String date, String showNotes) {
		this.number = number;
		this.date = date;
		this.showNotes = showNotes;
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
