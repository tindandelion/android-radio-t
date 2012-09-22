package org.dandelion.radiot.endtoend.podcasts.helpers;

import android.app.Instrumentation;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

public class PodcastListRunner {
    private PodcastListDriver driver;

    public PodcastListRunner(Instrumentation instrumentation, PodcastListActivity activity) {
        this.driver = new PodcastListDriver(instrumentation, activity);
    }

    public void showsPodcastItem(String number, String date, String description) {
        driver.showsItemWith(number, date, description);
    }

    public void pressBack() {
        driver.goBack();
    }

    public void wasClosed() {
        driver.wasClosed();
    }

    public void showsEmptyList() {
        driver.showsEmptyList();
    }

    public void refreshPodcasts() {
        driver.refreshPodcasts();
    }
}
