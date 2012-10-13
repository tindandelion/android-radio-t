package org.dandelion.radiot.endtoend.podcasts;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import org.dandelion.radiot.endtoend.podcasts.helpers.PodcastListRunner;
import org.dandelion.radiot.endtoend.podcasts.helpers.TestRssServer;
import org.dandelion.radiot.podcasts.core.*;
import org.dandelion.radiot.podcasts.loader.*;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;

import java.io.File;

import static org.dandelion.radiot.endtoend.podcasts.helpers.RssFeedBuilder.rssFeed;
import static org.dandelion.radiot.endtoend.podcasts.helpers.RssFeedBuilder.rssItem;
import static org.dandelion.radiot.helpers.PodcastListBuilder.aListWith;
import static org.dandelion.radiot.helpers.PodcastListBuilder.aPodcastItem;

public class PodcastListLoadingTests
        extends ActivityInstrumentationTestCase2<PodcastListActivity> {

    private TestRssServer backend;
    private PodcastListRunner app;
    private TestPodcastsPlatform platform;

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

    public void testWhenHasExpiredCache_ShouldDisplayItPriorToRequestingTheServer() throws Exception {
        final PodcastList localCachedList = aListWith(aPodcastItem("Радио-Т 140"));
        final String feedOnServer = rssFeed()
                .item(rssItem().title("Радио-Т 141"))
                .done();

        platform.saveInLocalCache(localCachedList);
        platform.expireCache();

        app = startApplication();
        app.showsPodcastItem("#140", "", "");

        backend.hasReceivedRequestForRss();
        backend.respondSuccessWith(feedOnServer);

        app.showsPodcastItem("#141", "", "");
    }

    public void testWhenHasFreshCache_ShouldDisplayItAndNotRequestTheServer() throws Exception {
        final PodcastList localCachedList = aListWith(aPodcastItem("Радио-Т 140"));

        platform.saveInLocalCache(localCachedList);

        app = startApplication();
        app.showsPodcastItem("#140", "", "");
        backend.hasNotReceivedAnyRequest();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new TestRssServer();
        platform = new TestPodcastsPlatform(context());
        PodcastListActivity.loaderFactory = platform;
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

    private static class TestPodcastsPlatform implements LoaderFactory {
        private static String CACHE_FILENAME = "test-show";
        private static final String BASE_URL = TestRssServer.addressForUrl("");
        private static final String RSS_URL = TestRssServer.addressForUrl("/rss");
        private static final int CACHE_FORMAT_VERSION = 0;
        private static final int LONG_AGO = 0;
        private PodcastsCache localCache;
        private Context context;

        public TestPodcastsPlatform(Context context) {
            this.context = context;
            localCache = new FilePodcastsCache(cacheFile(), CACHE_FORMAT_VERSION);
            localCache.reset();
        }

        private File cacheFile() {
            return new File(context.getCacheDir(), CACHE_FILENAME);
        }

        @Override
        public PodcastListLoader createLoaderForShow(String name) {
            HttpThumbnailProvider thumbnails = new HttpThumbnailProvider(BASE_URL);
            RssFeedProvider rssProvider = new RssFeedProvider(RSS_URL, thumbnails);
            return new AsyncPodcastListLoader(rssProvider, localCache);
        }

        public void saveInLocalCache(PodcastList pl) {
            localCache.updateWith(pl);
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public void expireCache() {
            cacheFile().setLastModified(LONG_AGO);
        }
    }
}
