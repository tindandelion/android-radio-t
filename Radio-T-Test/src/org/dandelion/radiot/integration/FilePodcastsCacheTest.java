package org.dandelion.radiot.integration;

import android.content.Context;
import android.test.InstrumentationTestCase;
import org.dandelion.radiot.podcasts.core.FilePodcastsCache;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilePodcastsCacheTest extends InstrumentationTestCase {
    private static final int FORMAT_VERSION = 42;

    private FilePodcastsCache cache;
    private File cacheFile;

    public void testSavingAndLoadingPodcastList() throws Exception {
        final PodcastItem original = aPodcastItem();

        cache.updateWith(aListWith(original));
        PodcastList cachedList = cache.getData();

        PodcastItem restored = cachedList.first();
        assertThat(restored.getTitle(), equalTo(original.getTitle()));
    }


    public void testResettingTheCache() throws Exception {
        cache.updateWith(aListWith(aPodcastItem()));

        cache.reset();

        assertThat(cache, not(valid()));
    }

    public void testResettingTheInvalidCacheIsNotAnError() throws Exception {
        cache.reset();
        assertThat(cache, not(valid()));
    }

    public void testCacheIsValidWhenItHasData() throws Exception {
        assertThat(cache, not(valid()));
        cache.updateWith(aListWith(aPodcastItem()));
        assertThat(cache, is(valid()));
    }

    public void testCacheIsInvalidIfFileIsCorrupt() throws Exception {
        createValidCacheFile(FORMAT_VERSION);

        writeCacheFile("Some junk content");
        assertThat(cache, not(valid()));
    }

    public void testCacheIsInvalidIfOlderThanOneDay() throws Exception {
        createValidCacheFile(FORMAT_VERSION);
        assertThat(cache, is(valid()));

        cacheCreated(hoursAgo(23));
        assertThat(cache, is((valid())));

        cacheCreated(hoursAgo(25));
        assertThat(cache, is(not(valid())));
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
        assertThat(cache, not(valid()));
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

    private PodcastItem aPodcastItem() {
        final PodcastItem original = new PodcastItem();
        original.setTitle("Title");
        return original;
    }


    private Matcher<? super FilePodcastsCache> valid() {
        return new TypeSafeMatcher<FilePodcastsCache>() {
            @Override
            protected boolean matchesSafely(FilePodcastsCache cache) {
                return cache.isValid();
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

    private PodcastList aListWith(final PodcastItem original) {
        return new PodcastList() {{
                add(original);
            }};
    }


}
