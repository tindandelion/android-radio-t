package org.dandelion.radiot.live;

public class ShowTopic {

	public String title;
	public String url;

	public ShowTopic(String title, String url) {
		this.title = title;
		this.url = url;
	}
	
	@Override
	public String toString() {
		return title;
	}
}
