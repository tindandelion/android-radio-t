package org.dandelion.radiot.helpers;

import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;

public class PodcastDataBuilder {

    public static PodcastList anEmptyList() {
        return new PodcastList();
    }

    public static PodcastList aListWith(final PodcastItem item) {
        PodcastList list = anEmptyList();
        list.add(item);
        return list;
    }

    public static PodcastItem aPodcastItem() {
        return new PodcastItem();
    }

    public static PodcastItem aPodcastItem(ItemBuilder builder) {
        return builder.done();
    }

    public static ItemBuilder withThumbnailUrl(String url) {
        return new ItemBuilder().setThumbnailUrl(url);
    }

    public static ItemBuilder withTitle(String title) {
        return new ItemBuilder().setTitle(title);
    }

    static class ItemBuilder {
        private final PodcastItem item = new PodcastItem();

        public ItemBuilder setThumbnailUrl(String url) {
            item.thumbnailUrl = url;
            return this;
        }

        public PodcastItem done() {
            return item;
        }

        public ItemBuilder setTitle(String title) {
            item.title = title;
            return this;
        }
    }
}
