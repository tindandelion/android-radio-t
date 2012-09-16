package org.dandelion.radiot.endtoend.podcasts;

import android.app.Instrumentation;
import android.widget.ListView;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class PodcastListRunner {
    private PodcastListDriver driver;

    public PodcastListRunner(Instrumentation instrumentation, PodcastListActivity activity) {
        this.driver = new PodcastListDriver(instrumentation, activity);
    }

    public void showsEmptyPodcastList() {
        driver.podcastList(hasCount(0));
    }

    public void showsPodcastListWithItemCount(int count) {
        driver.podcastList(hasCount(count));
    }

    public void showsPodcastItem(String number, String date, String description) {
        driver.firstItemShows(number, date, description);
    }

    private Matcher<? super ListView> hasCount(int count) {
        return new FeatureMatcher<ListView, Integer>(equalTo(count),
                "Podcast count", "count") {
            @Override
            protected Integer featureValueOf(ListView listView) {
                return listView.getCount();
            }
        };
    }

}
