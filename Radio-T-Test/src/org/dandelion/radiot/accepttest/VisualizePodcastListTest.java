package org.dandelion.radiot.accepttest;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.PodcastListRunner;
import org.dandelion.radiot.accepttest.testables.TestRssServer;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

public class VisualizePodcastListTest
        extends ActivityInstrumentationTestCase2<PodcastListActivity> {
    public VisualizePodcastListTest() {
        super(PodcastListActivity.class);
    }

    public void testShowsTheCorrectNumberOfPodcasts() throws Exception {
        TestRssServer backend = new TestRssServer();
        PodcastListRunner app = new PodcastListRunner(getInstrumentation(), startActivity());
        backend.provideRssFeedWithItemCount(20);
        app.showsPodcastsInCount(20);
    }

    private PodcastListActivity startActivity() {
        setActivityIntent(startupIntent());
        return getActivity();
    }

    private Intent startupIntent() {
        Context context = getInstrumentation().getContext();
        return PodcastListActivity.createIntent(context, "Test Activity", "test-show");
    }

}
