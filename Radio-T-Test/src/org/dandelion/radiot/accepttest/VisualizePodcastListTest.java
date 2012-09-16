package org.dandelion.radiot.accepttest;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import org.dandelion.radiot.accepttest.drivers.PodcastListRunner;
import org.dandelion.radiot.accepttest.testables.TestRssServer;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class VisualizePodcastListTest
        extends ActivityInstrumentationTestCase2<PodcastListActivity> {
    private TestRssServer backend;

    public VisualizePodcastListTest() {
        super(PodcastListActivity.class);
    }

    public void testNoPodcastsInList() throws Exception {
        backend.buildFeed()
                .empty()
                .done();

        PodcastListRunner app = startApplication();
        app.assertPodcastList(isEmpty());
    }

    public void testShowsTheCorrectNumberOfPodcasts() throws Exception {
        backend.buildFeed()
                .items(5)
                .done();

        PodcastListRunner app = startApplication();
        app.assertPodcastList(hasCount(5));
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new TestRssServer();
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }

    private Matcher<? super ListView> isEmpty() {
        return hasCount(0);
    }

    private Matcher<? super ListView> hasCount(int expected) {
        return new FeatureMatcher<ListView, Integer>(equalTo(expected),
                "Podcast count equals", "Podcast count") {
            @Override
            protected Integer featureValueOf(ListView listView) {
                return listView.getCount();
            }
        };
    }

    private PodcastListRunner startApplication() {
        setActivityIntent(startupIntent());
        return new PodcastListRunner(getInstrumentation(), getActivity());
    }

    private Intent startupIntent() {
        Context context = getInstrumentation().getContext();
        return PodcastListActivity.createIntent(context, "Test Activity", "test-show");
    }

}
