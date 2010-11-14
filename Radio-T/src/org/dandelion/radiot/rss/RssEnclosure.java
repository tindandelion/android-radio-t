package org.dandelion.radiot.rss;

public class RssEnclosure {
	public String url = "";
	public String type = "";

	public RssEnclosure(String type, String url) {
		this.type = type;
		this.url = url;
	}

	public boolean hasType(String type) {
		return this.type.equals(type);
	}
}