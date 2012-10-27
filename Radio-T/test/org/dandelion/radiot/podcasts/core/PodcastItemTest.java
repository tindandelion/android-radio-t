package org.dandelion.radiot.podcasts.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PodcastItemTest {
    private final PodcastItem item = new PodcastItem();

    @Test
    public void extractPodcastNumberFromTitle() throws Exception {
        item.setTitle("Radio 100");
        assertEquals("#100", item.getNumberString());
    }

    @Test
    public void podcastNumberInLocalizedString() throws Exception {
        item.setTitle("Радио-Т 192");
        assertEquals("#192", item.getNumberString());
    }

    @Test
    public void noNumberInTitle() throws Exception {
        item.setTitle("Blah");
        assertEquals("Blah", item.getNumberString());
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
    public void extractThumbnailFromDescription() throws Exception {
        item.extractThumbnailUrl("<img src=\"http://radio-t.com/thumbnail.jpg\" />");
        assertEquals("http://radio-t.com/thumbnail.jpg", item.getThumbnailUrl());
    }

    @Test
    public void noThumbnailUrlInDescription() throws Exception {
        item.extractThumbnailUrl("Blah blah");
        assertNull(item.getThumbnailUrl());
    }

    @Test
    public void veryMessyThumbnailUrl() throws Exception {
        item.extractThumbnailUrl("<img    src=\"http://radio-t.com/thumbnail.jpg\" alt=\"thumbnail\" width=\"100\"/>");
        assertEquals("http://radio-t.com/thumbnail.jpg", item.getThumbnailUrl());
    }
}
