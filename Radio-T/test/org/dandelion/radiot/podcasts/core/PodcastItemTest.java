package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PodcastItemTest {
    private final PodcastItem item = new PodcastItem();

    @Test
    public void simplePodcastNumber() throws Exception {
        item.setTitle("100");
        assertEquals("#100", item.getNumber());
    }

    @Test
    public void podcastNumberInString() throws Exception {
        item.setTitle("Radio 100");
        assertEquals("#100", item.getNumber());
    }

    @Test
    public void podcastNumberInLocalizedString() throws Exception {
        item.setTitle("Радио-Т 192");
        assertEquals("#192", item.getNumber());
    }

    @Test
    public void noNumberInTitle() throws Exception {
        item.setTitle("Blah");
        assertEquals("Blah", item.getNumber());
    }

    @Test
    public void extractPublicationDate() throws Exception {
        item.extractPubDate("Sun, 13 Jun 2010 01:37:22 +0000");
        assertEquals("13.06.2010", item.getPubDate());
    }

    @Test
    public void incorrectPublicationDate() throws Exception {
        item.extractPubDate("Blah");
        assertEquals("", item.getPubDate());
    }

    @Test
    public void trimShowNotes() throws Exception {
        String notes = "   Note 1 - Note 2   \n\n";
        item.extractShowNotes(notes);
        assertEquals("Note 1 - Note 2", item.getShowNotes());
    }

    @Test
    public void constructThumbnailUrlFromPodcastNumber() throws Exception {
        item.setTitle("Radio-T 100");
        assertEquals("http://www.radio-t.com/images/radio-t/rt100.jpg",
                item.getThumbnailUrl());
    }

    @Test
    public void noThumbnailUrlIfNoNumberIsInTitle() throws Exception {
        item.setTitle("Blah");
        assertNull(item.getThumbnailUrl());
    }
}
