package org.dandelion.radiot.integration;

import org.dandelion.radiot.helpers.TestableCacheFile;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.caching.FilePodcastsCache;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.dandelion.radiot.util.PodcastDataBuilder.aListWith;
import static org.dandelion.radiot.util.PodcastDataBuilder.aPodcastItem;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilePodcastsCacheTest extends FilesystemTestCase {
    private static final int FORMAT_VERSION = 42;

    private FilePodcastsCache cache;
    private TestableCacheFile cacheFile;

    public void testWhenHasNoData_ReturnsEmptyList() throws Exception {
        PodcastList cachedList = cache.getData();
        assertThat(cachedList, is(empty()));
    }

    public void testWhenProvidedWithData_ShouldReturnItBack() throws Exception {
        final PodcastItem original = aPodcastItem();

        cache.updateWith(aListWith(original));
        PodcastList cachedList = cache.getData();

        PodcastItem restored = cachedList.first();
        assertThat(restored.title, equalTo(original.title));
    }

    public void testWhenCacheIsReset_ShouldLoseData() throws Exception {
        cache.updateWith(aListWith(aPodcastItem()));

        cache.reset();
        assertThat(cache, not(hasValidData()));
    }

    public void testResettingCacheWithNoData_IsNotAnError() throws Exception {
        cache.reset();
        cache.reset();
        assertThat(cache, not(hasValidData()));
    }

    public void testCacheHasValidData() throws Exception {
        assertThat(cache, not(hasValidData()));
        cache.updateWith(aListWith(aPodcastItem()));
        assertThat(cache, hasValidData());
    }

    public void testWhenFileIsCorrupted_CacheHasNoData() throws Exception {
        createValidCacheFile(FORMAT_VERSION);

        cacheFile.write("Some junk content");
        assertThat(cache, not(hasValidData()));
    }

    public void testWhenCreatedMoreThanOneDayAgo_ShouldBecomeExpired() throws Exception {
        createValidCacheFile(FORMAT_VERSION);
        assertThat(cache, hasValidData());

        cacheCreated(hoursAgo(23));
        assertThat(cache, hasValidData());

        cacheCreated(hoursAgo(25));
        assertThat(cache, not(hasValidData()));
    }

    public void testWhenUpdatingCache_InvokeListener() throws Exception {
        FakeListener listener = new FakeListener();
        cache.setListener(listener);

        PodcastList list = aListWith(aPodcastItem());
        cache.updateWith(list);
        assertThat(listener.updatedList, equalTo(list));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void cacheCreated(long timestamp) {
        cacheFile.setLastModified(timestamp);
    }

    private long hoursAgo(int hours) {
        long current = System.currentTimeMillis();
        int millis = (hours * 3600 * 1000);
        return current - millis;
    }

    private void createValidCacheFile(int formatVersion) {
        newCacheInstance(formatVersion).updateWith(aListWith(aPodcastItem()));
    }

    private FilePodcastsCache newCacheInstance(int formatVersion) {
        return new FilePodcastsCache(cacheFile, formatVersion);
    }

    public void testCacheIsInvalidIfFileIsWrongVersion() throws Exception {
        final int olderVersion = FORMAT_VERSION - 1;
        createValidCacheFile(olderVersion);
        assertThat(cache, not(hasValidData()));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void setUp() throws Exception {
        super.setUp();
        cacheFile = new TestableCacheFile(cacheDir(), "test-cache");
        cacheFile.delete();

        cache = newCacheInstance(FORMAT_VERSION);
    }

    private Matcher<PodcastList> empty() {
        return new TypeSafeMatcher<PodcastList>() {
            @Override
            protected boolean matchesSafely(PodcastList pl) {
                return pl.size() == 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an empty podcast list");
            }
        };
    }

    private Matcher<FilePodcastsCache> hasValidData() {
        return new TypeSafeMatcher<FilePodcastsCache>() {
            @Override
            protected boolean matchesSafely(FilePodcastsCache cache) {
                return cache.hasValidData();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a cache with valid data");
            }

            @Override
            protected void describeMismatchSafely(FilePodcastsCache item, Description mismatchDescription) {
                mismatchDescription.appendText("a cache with no data");
            }
        };
    }

    private class FakeListener implements FilePodcastsCache.Listener {
        public PodcastList updatedList;

        @Override
        public void onUpdatedWith(PodcastList list) {
            updatedList = list;
        }
    }

}
