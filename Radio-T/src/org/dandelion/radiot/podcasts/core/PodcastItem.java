package org.dandelion.radiot.podcasts.core;

import java.io.Serializable;

public class PodcastItem implements Cloneable, Serializable {

    public String pubDate = "";
	public String showNotes = "";
	public String audioUri = "";
    public String title = "";
    public String thumbnailUrl = null;

    public boolean hasThumbnail() {
        return thumbnailUrl != null;
    }
}
