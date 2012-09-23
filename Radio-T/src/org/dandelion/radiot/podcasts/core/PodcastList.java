package org.dandelion.radiot.podcasts.core;

import java.util.ArrayList;
import java.util.List;

public class PodcastList extends ArrayList<PodcastItem> {
    public PodcastList(List<PodcastItem> l) {
        super(l);
    }

    public PodcastList() {
        super();
    }
}
