package org.dandelion.radiot;

public class PodcastItem {
	private String number;

	public PodcastItem(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}
	
	@Override
	public String toString() {
		return number;
	}
}
