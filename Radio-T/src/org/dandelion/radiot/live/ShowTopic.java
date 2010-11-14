package org.dandelion.radiot.live;


public class ShowTopic {
	public String title;

	public ShowTopic(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}
}
