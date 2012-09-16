package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

import static org.hamcrest.CoreMatchers.equalTo;

public class PodcastListRunner {
    private PodcastListUiDriver driver;

    public PodcastListRunner(Instrumentation instrumentation, PodcastListActivity activity) {
        this.driver = new PodcastListUiDriver(instrumentation, activity);
    }

    public void showsEmptyPodcastList() {
        driver.podcastListCount(equalTo(0));
    }

    public void showsPodcastListWithItemCount(int count) {
        driver.podcastListCount(equalTo(count));
    }
}
