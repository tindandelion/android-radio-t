package org.dandelion.radiot.integration;

import android.content.Context;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.podcasts.loader.caching.FilePodcastsCache;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.dandelion.radiot.helpers.PodcastListBuilder.aListWith;
import static org.dandelion.radiot.helpers.PodcastListBuilder.aPodcastItem;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilePodcastsCacheTest extends InstrumentationTestCase {
    private static final int FORMAT_VERSION = 42;

    private FilePodcastsCache cache;
    private File cacheFile;

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

        assertThat(cache, not(hasData()));
    }

    public void testResettingCacheWithNoData_IsNotAnError() throws Exception {
        cache.reset();
        cache.reset();
        assertThat(cache, not(hasData()));
    }

    public void testCacheHasValidData() throws Exception {
        assertThat(cache, not(hasData()));
        cache.updateWith(aListWith(aPodcastItem()));
        assertThat(cache, hasData());
    }

    public void testWhenFileIsCorrupted_CacheHasNoData() throws Exception {
        createValidCacheFile(FORMAT_VERSION);

        writeCacheFile("Some junk content");
        assertThat(cache, not(hasData()));
    }

    public void testWhenCreatedMoreThanOneDayAgo_ShouldBecomeExpired() throws Exception {
        createValidCacheFile(FORMAT_VERSION);
        assertThat(cache, not(expired()));

        cacheCreated(hoursAgo(23));
        assertThat(cache, not(expired()));

        cacheCreated(hoursAgo(25));
        assertThat(cache, is(expired()));
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
        new FilePodcastsCache(cacheFile, formatVersion).updateWith(aListWith(aPodcastItem()));
    }

    private void writeCacheFile(String content) throws IOException {
        FileWriter out = new FileWriter(cacheFile);
        out.write(content);
    }

    public void testCacheIsInvalidIfFileIsWrongVersion() throws Exception {
        final int olderVersion = FORMAT_VERSION - 1;
        createValidCacheFile(olderVersion);
        assertThat(cache, not(hasData()));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getTargetContext();
        cacheFile = new File(context.getCacheDir(), "test-cache");
        if (cacheFile.exists()) {
            cacheFile.delete();
        }

        cache = new FilePodcastsCache(cacheFile, FORMAT_VERSION);
    }

    private Matcher<? super FilePodcastsCache> hasData() {
        return new TypeSafeMatcher<FilePodcastsCache>() {
            @Override
            protected boolean matchesSafely(FilePodcastsCache cache) {
                return cache.hasData();
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

    private Matcher<? super FilePodcastsCache> expired() {
        return new TypeSafeMatcher<FilePodcastsCache>() {
            @Override
            protected boolean matchesSafely(FilePodcastsCache expired) {
                return expired.hasExpired();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a cache that is expired");
            }
        };
    }
}
