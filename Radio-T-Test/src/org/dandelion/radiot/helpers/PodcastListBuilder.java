package org.dandelion.radiot.helpers;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class PodcastListBuilder {
    public static PodcastList aListWith(final PodcastItem item) {
        return new PodcastList() {{
            add(item);
        }};
    }

    public static PodcastItem aPodcastItem() {
        return aPodcastItem("Title");
    }

    public static PodcastItem aPodcastItem(String title) {
        final PodcastItem pi = new PodcastItem();
        pi.setTitle(title);
        return pi;
    }
}
