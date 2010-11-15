package org.dandelion.radiot.rss;

import java.util.ArrayList;
import java.util.List;

public class RssItem {
	public String title = "";
	public String pubDate = "";
	public String description = "";
	public String encodedContent = "";
	public String link = "";
	public ArrayList<String> categories = new ArrayList<String>();
	public ArrayList<RssEnclosure> enclosures = new ArrayList<RssEnclosure>();
	
	public RssItem() { 
		
	}

	public RssItem(String title) {
		this.title = title;
	}

	public void addCategory(String value) {
		categories.add(value);
	}

	public void addEnclosure(RssEnclosure enclosure) {
		enclosures.add(enclosure);
	}

	public List<RssEnclosure> getEnclosures(String type) {
		List<RssEnclosure> result = new ArrayList<RssEnclosure>();
		for (RssEnclosure item : enclosures) {
			if (item.hasType(type)) {
				result.add(item);
			}
		}

		return result;
	}

	public boolean hasCategory(String value) {
		return categories.contains(value);
	}
}