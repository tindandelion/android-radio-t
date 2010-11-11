package org.dandelion.radiot.live;

import android.net.Uri;

public class ShowTopic {

	public String title;
	public Uri uri;

	public ShowTopic(String title, String url) {
		this.title = title;
		this.uri = Uri.parse(url);
	}
	
	@Override
	public String toString() {
		return title;
	}
}
