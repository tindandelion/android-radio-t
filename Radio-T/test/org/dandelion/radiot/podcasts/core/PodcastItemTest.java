package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PodcastItemTest {
    private final PodcastItem item = new PodcastItem();

    @Test
    public void extractThumbnailFromDescription() throws Exception {
        item.extractThumbnailUrl("<img src=\"http://radio-t.com/thumbnail.jpg\" />");
        assertEquals("http://radio-t.com/thumbnail.jpg", item.thumbnailUrl);
    }

    @Test
    public void noThumbnailUrlInDescription() throws Exception {
        item.extractThumbnailUrl("Blah blah");
        assertNull(item.thumbnailUrl);
    }

    @Test
    public void veryMessyThumbnailUrl() throws Exception {
        item.extractThumbnailUrl("<img    src=\"http://radio-t.com/thumbnail.jpg\" alt=\"thumbnail\" width=\"100\"/>");
        assertEquals("http://radio-t.com/thumbnail.jpg", item.thumbnailUrl);
    }
}
