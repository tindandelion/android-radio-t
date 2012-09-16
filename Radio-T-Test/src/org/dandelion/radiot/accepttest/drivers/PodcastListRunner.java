package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import junit.framework.Assert;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

public class PodcastListRunner {
    private PodcastListUiDriver driver;

    public PodcastListRunner(Instrumentation instrumentation, PodcastListActivity activity) {
        this.driver = new PodcastListUiDriver(instrumentation, activity);
    }

    public void showsPodcastsInCount(int expectedCount) {
        Assert.assertEquals("Podcast count mismatch", expectedCount, driver.listView().getCount());
    }
}
