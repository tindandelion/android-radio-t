package org.dandelion.radiot.integration;

import android.graphics.drawable.Drawable;
import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.ui.PodcastVisual;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PodcastVisualTest extends TestCase {

    private PodcastItem item;

    public void testWhenPubDateIsValid_ConvertsItToShortFormat() throws Exception {
        item.pubDate = "Sat, 03 Nov 2012 15:08:00 PDT";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.pubDate, equalTo("04.11.2012"));
    }

    public void testWhenDateIsInvalid_displaysEmptyString() throws Exception {
        item.pubDate = "Blah";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.pubDate, equalTo(""));
    }

    public void testTrimShowNotes() throws Exception {
        item.showNotes = "    Show notes    ";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.showNotes, equalTo("Show notes"));
    }

    public void testWhenNoNumberInTitle_displaysTitleItself() throws Exception {
        item.title = "Blah";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("Blah"));
    }

    public void testWhenNumberIsInTitle_extractsIt() throws Exception {
        item.title = "Radio-T 150";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("#150"));
    }



    private PodcastVisual newVisual(PodcastItem item) {
        final Drawable thumbnail = null;
        return new PodcastVisual(item, thumbnail);
    }

    protected void setUp() throws Exception {
        super.setUp();
        item = new PodcastItem();
    }
}
