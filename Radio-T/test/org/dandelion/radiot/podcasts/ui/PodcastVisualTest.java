package org.dandelion.radiot.podcasts.ui;

import android.graphics.drawable.Drawable;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PodcastVisualTest {
    private PodcastItem item = new PodcastItem();

    @Test
    public void whenPubDateIsValid_convertsItToShortFormat() throws Exception {
        item.pubDate = "Sat, 03 Nov 2012 15:08:00 PDT";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.pubDate, equalTo("04.11.2012"));
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

    @Test
    public void whenNoNumberInTitle_displaysTitleItself() throws Exception {
        item.title = "Blah";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("Blah"));
    }

    @Test
    public void whenNumberIsInTitle_extractsIt() throws Exception {
        item.title = "Radio-T 150";
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("● 150"));
    }

    private PodcastVisual newVisual(PodcastItem item) {
        final Drawable thumbnail = null;
        return new PodcastVisual(item);
    }
}
