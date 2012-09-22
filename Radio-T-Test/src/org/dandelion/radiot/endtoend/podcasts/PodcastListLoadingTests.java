package org.dandelion.radiot.endtoend.podcasts;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.podcasts.helpers.PodcastListRunner;
import org.dandelion.radiot.endtoend.podcasts.helpers.TestRssServer;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

import static org.dandelion.radiot.endtoend.podcasts.helpers.RssFeedBuilder.buildFeed;

public class PodcastListLoadingTests
        extends ActivityInstrumentationTestCase2<PodcastListActivity> {
    private TestRssServer backend;
    private PodcastListRunner app;

    public PodcastListLoadingTests() {
        super(PodcastListActivity.class);
    }

    public void testRetrievePodcastListFromRssServerAndDisplayIt() throws Exception {
        String feed = buildFeed()
                .item("<title>Радио-Т 140</title>" +
                        "<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>" +
                        "<itunes:summary>Lorem ipsum dolor sit amet</itunes:summary>")
                .item("<title>Радио-Т 141</title>" +
                        "<pubDate>Sun, 19 Jun 2010 01:37:22 +0000</pubDate>" +
                        "<itunes:summary>consectetur adipiscing elit</itunes:summary>")
                .done();

        app = startApplication();
        backend.hasReceivedRequest();
        backend.respondWith(feed);

        app.showsPodcastItem("#140", "13.06.2010", "Lorem ipsum dolor sit amet");
        app.showsPodcastItem("#141", "19.06.2010", "consectetur adipiscing elit");
    }

    public void testPressingBackButtonWhileLoadingClosesActivity() throws Exception {
        app = startApplication();
        backend.hasReceivedRequest();

        app.pressBack();
        app.wasClosed();
    }

    public void testRefreshingPodcastListRetrievesItFromServerAgain() throws Exception {
        String initialFeed = buildFeed().done();
        String updatedFeed = buildFeed()
                .item("<title>Радио-Т 140</title>" +
                        "<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>" +
                        "<itunes:summary>Lorem ipsum dolor sit amet</itunes:summary>")
                .done();

        backend.respondWith(initialFeed);

        app = startApplication();
        app.showsEmptyList();

        app.refreshPodcasts();
        backend.hasReceivedRequest();
        backend.respondWith(updatedFeed);

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
