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
        item.setTitle("Radio-T 150");
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("#150"));
    }

    @Test
    public void whenNoNumberInTitle_displaysTitleItself() throws Exception {
        item.setTitle("Blah");
        PodcastVisual visual = newVisual(item);
        assertThat(visual.number, equalTo("Blah"));
    }

    private PodcastVisual newVisual(PodcastItem item) {
        final Drawable thumbnail = null;
        return new PodcastVisual(item, thumbnail);
    }
}
