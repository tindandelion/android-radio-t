package org.dandelion.radiot.live;

import org.dandelion.radiot.rss.RssItem;


public class ShowTopic {
	public String title;

	public ShowTopic(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}

	public static ShowTopic fromRss(RssItem item) {
		return new ShowTopic(item.title);
	}
}
