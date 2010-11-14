package org.dandelion.radiot.helpers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dandelion.radiot.rss.IFeedSource;

public class FakeFeedSource implements IFeedSource {
	private String feedContent = "";

	public InputStream openFeedStream() throws IOException {
		return new ByteArrayInputStream(getCompleteFeed().getBytes());
	}

	public String getCompleteFeed() {
		return getFeedContent();
	}

	public void setFeedContent(String value) {
		feedContent = value;
	}
	
	public String getFeedContent() {
		return feedContent;
	}
}