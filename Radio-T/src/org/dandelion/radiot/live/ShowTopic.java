package org.dandelion.radiot.live;

import org.dandelion.radiot.rss.RssItem;

import android.net.Uri;


public class ShowTopic {
	public String title;
	private String url;

	public ShowTopic(String title) {
		this.title = title;
	}
	
	public ShowTopic(String title, String url) {
		this.title = title;
		this.url = url;
	}

	@Override
	public String toString() {
		return title;
	}
	
	public Uri getUri() {
		return Uri.parse(url);
	}

	public static ShowTopic fromRss(RssItem item) {
		return new ShowTopic(item.title, item.link);
	}
}