package org.dandelion.radiot.accepttest.drivers;

import android.app.Instrumentation;
import android.widget.ListView;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

public class PodcastListRunner {
    private PodcastListUiDriver driver;

    public PodcastListRunner(Instrumentation instrumentation, PodcastListActivity activity) {
        this.driver = new PodcastListUiDriver(instrumentation, activity);
    }

    public void assertPodcastList(Matcher<? super ListView> matcher) {
        assertThat(driver.listView(), matcher);
    }
}
