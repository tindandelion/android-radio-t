package org.dandelion.radiot.endtoend.podcasts;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.podcasts.helpers.PodcastListRunner;
import org.dandelion.radiot.endtoend.podcasts.helpers.TestRssServer;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

import static org.dandelion.radiot.endtoend.podcasts.helpers.RssFeedBuilder.rssFeed;
import static org.dandelion.radiot.endtoend.podcasts.helpers.RssFeedBuilder.rssItem;
import static org.dandelion.radiot.helpers.PodcastListBuilder.aListWith;
import static org.dandelion.radiot.helpers.PodcastListBuilder.aPodcastItem;

public class PodcastListLoadingTests
        extends ActivityInstrumentationTestCase2<PodcastListActivity> {
    private static final int PORT = 32768;

    private TestRssServer backend;
    private PodcastListRunner app;
    private TestPodcastsApp platform;

    public PodcastListLoadingTests() {
        super(PodcastListActivity.class);
    }

    public void testGivenAFeed_ShouldRetrieveAndDisplayIt() throws Exception {
        String feed = rssFeed()
                .item(rssItem()
                        .title("Радио-Т 140")
                        .pubDate("Sun, 13 Jun 2010 01:37:22 +0000")
                        .summary("Lorem ipsum dolor sit amet"))
                .item(rssItem()
                        .title("Радио-Т 141")
                        .pubDate("Sun, 19 Jun 2010 01:37:22 +0000")
                        .summary("consectetur adipiscing elit"))
                .done();

        app = startApplication();
        backend.hasReceivedRequestForRss();
        backend.respondSuccessWith(feed);

        app.showsPodcastItem("#140", "13.06.2010", "Lorem ipsum dolor sit amet");
        app.showsPodcastItem("#141", "19.06.2010", "consectetur adipiscing elit");
    }

    public void testPressingbackButtonWhileLoading_ShouldCloseActivity() throws Exception {
        app = startApplication();
        backend.hasReceivedRequestForRss();

        app.pressBack();
        app.wasClosed();
    }

    public void testGivenAFeedItemWithThumbnailLink_ShouldRetrieveItsContents() throws Exception {
        final String thumbnailUrl = "/images/radio-t/rt307.jpg";
        final String feed = rssFeed()
                .item(rssItem()
                        .title("Радио-Т 140")
                        .pubDate("Sun, 13 Jun 2010 01:37:22 +0000")
                        .description("&lt;p&gt;&lt;img src=\"" +
                                thumbnailUrl + "\" alt=\"\" /&gt;&lt;/p&gt;"))
                .done();

        app = startApplication();
        backend.hasReceivedRequestForRss();
        backend.respondSuccessWith(feed);

        backend.hasReceivedRequestForUrl(thumbnailUrl);
    }

    public void testWhenRequestedToRefreshList_ShouldRetrieveItFromServer() throws Exception {
        final String initialFeed = rssFeed().done();
        final String updatedFeed = rssFeed()
                .item(rssItem()
                        .title("Радио-Т 140")
                        .pubDate("Sun, 13 Jun 2010 01:37:22 +0000")
                        .summary("Lorem ipsum dolor sit amet"))
                .done();

        backend.respondSuccessWith(initialFeed);

        app = startApplication();
        app.showsEmptyList();

        app.refreshPodcasts();
        backend.hasReceivedRequestForRss();
        backend.respondSuccessWith(updatedFeed);

        app.showsPodcastItem("#140", "13.06.2010", "Lorem ipsum dolor sit amet");
    }

    public void testWhenServerRespondsWithError_ShouldDisplayItOnScreen() throws Exception {
        backend.respondNotFoundError();
        app = startApplication();
        app.showsErrorMessage();
    }

    public void testWhenHasLocalCache_ShouldDisplayItPriorToRequestingTheServer() throws Exception {
        final PodcastList localCachedList = aListWith(aPodcastItem("Радио-Т 140"));
        final String feedOnServer = rssFeed()
                .item(rssItem()
                        .title("Радио-Т 141"))
                .done();

        platform.saveInLocalCache(localCachedList);

        app = startApplication();
        app.showsPodcastItem("#140", "", "");

        backend.hasReceivedRequestForRss();
        backend.respondSuccessWith(feedOnServer);

        app.showsPodcastItem("#141", "", "");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new TestRssServer(PORT);
        platform = new TestPodcastsApp(context());
        PodcastsApp.setTestingInstance(platform);
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
        return PodcastListActivity.createIntent(context(), "Test Activity", "test-show");
    }

    private Context context() {
        return getInstrumentation().getTargetContext();
    }

    private static class TestPodcastsApp extends PodcastsApp {
        private static String CACHE_FILENAME = "test-show";
        private static final String BASE_URL = String.format("http://localhost:%d", PORT);
        private static final String RSS_URL = BASE_URL + "/rss";
        public PodcastsCache localCache;

        public TestPodcastsApp(Context context) {
            super(context);
            localCache = super.createPodcastsCache(CACHE_FILENAME);
            localCache.reset();
        }

        @Override
        public PodcastListLoader createLoaderForShow(String name) {
            HttpThumbnailProvider thumbnails = new HttpThumbnailProvider(BASE_URL);
            return new AsyncPodcastListLoader(
                    new RssFeedProvider(RSS_URL, thumbnails),
                    localCache);
        }

        public void saveInLocalCache(PodcastList pl) {
            localCache.updateWith(pl);
        }
    }
}
