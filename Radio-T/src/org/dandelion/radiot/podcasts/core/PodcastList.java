package org.dandelion.radiot.podcasts.core;

import java.util.ArrayList;
import java.util.Iterator;

public class PodcastList implements Iterable<PodcastItem> {
    private final ArrayList<PodcastItem> items = new ArrayList<PodcastItem>();

    @Override
    public Iterator<PodcastItem> iterator() {
        return items.iterator();
    }

    public void add(PodcastItem item) {
        items.add(item);
    }

    public PodcastItem first() {
        if (!items.isEmpty())  {
            return items.get(0);
        }
        return null;
    }

    public int size() {
        return items.size();
    }
}
