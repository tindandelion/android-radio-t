package org.dandelion.radiot.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RemoteFeedSource implements IFeedSource {
	private String address;

	public RemoteFeedSource(String address) {
		this.address = address;
	}

	public InputStream openFeedStream() throws IOException {
		return new URL(address).openStream();
	}

}
