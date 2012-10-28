package org.dandelion.radiot.podcasts.ui;

import android.graphics.drawable.Drawable;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PodcastVisualTest {
    private final PodcastItem item = new PodcastItem();

    @Test
    public void whenNumberIsInTitle_extractsIt() throws Exception {
        item.title = "Radio-T 150";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("#150"));
    }

    @Test
    public void whenNoNumberInTitle_displaysTitleItself() throws Exception {
        item.title = "Blah";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("Blah"));
    }

    @Test
    public void whenDateIsValid_formatsItForDisplay() throws Exception {
        item.pubDate = "Sun, 13 Jun 2010 01:37:22 +0000";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.pubDate, equalTo("13.06.2010"));
    }

    @Test
    public void whenDateIsInvalid_displaysEmptyString() throws Exception {
        item.pubDate = "Blah";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.pubDate, equalTo(""));
    }

    @Test
    public void trimShowNotes() throws Exception {
        item.showNotes = "    Show notes    ";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.showNotes, equalTo("Show notes"));
    }

    private PodcastVisual newVisual(PodcastItem item) {
        final Drawable thumbnail = null;
        return new PodcastVisual(item, thumbnail);
    }
}
