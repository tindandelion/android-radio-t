package org.dandelion.radiot.accepttest;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.accepttest.drivers.PodcastListRunner;
import org.dandelion.radiot.accepttest.testables.TestRssServer;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

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
        app.showsEmptyPodcastList();
    }

    public void testShowsTheCorrectNumberOfPodcasts() throws Exception {
        backend.buildFeed()
                .items(5)
                .done();

        PodcastListRunner app = startApplication();
        app.showsPodcastListWithItemCount(5);
    }

    public void testDisplaysTextFieldsOfPodcastItem() throws Exception {
        backend.buildFeed()
                .item("<title>Радио-Т 140</title>" +
                        "<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>" +
                        "<itunes:summary>Lorem ipsum dolor sit amet</itunes:summary>")
                .done();
        PodcastListRunner app = startApplication();
        app.showsPodcastItem("#140", "13.06.2010", "Lorem ipsum dolor sit amet");
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

    private PodcastListRunner startApplication() {
        setActivityIntent(startupIntent());
        return new PodcastListRunner(getInstrumentation(), getActivity());
    }

    private Intent startupIntent() {
        Context context = getInstrumentation().getContext();
        return PodcastListActivity.createIntent(context, "Test Activity", "test-show");
    }

}
