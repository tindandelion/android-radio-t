package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class PodcastListTest {

    private final PodcastList list = new PodcastList();

    @Test
    public void collectThumbnails_returnsThumbnailUrlsAsList() throws Exception {
        list.add(podcastItemWithThumbnail("thumbnail1"));
        list.add(podcastItemWithThumbnail("thumbnail2"));

        List<String> thumbnails = list.collectThumbnails();
        assertThat(thumbnails, equalTo(Arrays.asList("thumbnail1", "thumbnail2")));
    }

    @Test
    public void collectThumbnails_skipsNullThumbnails() throws Exception {
        list.add(podcastItemWithThumbnail("thumbnail1"));
        list.add(podcastItemWithThumbnail(null));

        List<String> thumbnails = list.collectThumbnails();
        assertThat(thumbnails, equalTo(Arrays.asList("thumbnail1")));
    }

    private PodcastItem podcastItemWithThumbnail(String value) {
        PodcastItem item = new PodcastItem();
        item.thumbnailUrl = value;
        return item;
    }
}
