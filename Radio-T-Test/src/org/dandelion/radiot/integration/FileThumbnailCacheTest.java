package org.dandelion.radiot.integration;

import android.test.InstrumentationTestCase;
import org.dandelion.radiot.integration.helpers.FileUtils;
import org.dandelion.radiot.podcasts.loader.caching.FileThumbnailCache;

import java.io.*;
import java.util.Arrays;

public class FileThumbnailCacheTest extends InstrumentationTestCase {
    public static final byte[] THUMBNAIL = new byte[] {0x1, 0x2, 0x3};
    private FileThumbnailCache cache;

    public void testWhenNoThumbnailInCache_ReturnsNull() throws Exception {
        final String url = "http://example.com/thumbnail.jpg";
        assertNull(cache.lookup(url));
    }

    public void testWhenHasThumbnailInCache_ReturnItsContent() throws Exception {
        final String url = "http://example.com/thumbnail.jpg";

        cache.update(url, THUMBNAIL);
        byte[] cached = cache.lookup(url);

        assertTrue(Arrays.equals(THUMBNAIL, cached));
    }

    public void testWhenUrlIsEmpty_NeitherSaveNorLookItUp() throws Exception {
        final String url = "";

        cache.update(url, THUMBNAIL);
        assertNull(cache.lookup(url));
    }

    public void testWhenReachesTheCacheLimit_DeletesTheOldestThumbnail() throws Exception {
        cache = new FileThumbnailCache(prepareCacheDir(), 3);
        cache.update("/thumbnail1.jpg", THUMBNAIL);

        forceDelay();
        cache.update("/thumbnail2.jpg", THUMBNAIL);

        forceDelay();
        cache.update("/thumbnail3.jpg", THUMBNAIL);

        forceDelay();
        cache.update("/thumbnail4.jpg", THUMBNAIL);
        assertNull(cache.lookup("/thumbnail1.jpg"));

        forceDelay();
        cache.update("/thumbnail5.jpg", THUMBNAIL);
        assertNull(cache.lookup("/thumbnail2.jpg"));
    }

    private void forceDelay() throws InterruptedException {
        Thread.sleep(1000);
    }

    protected void setUp() throws Exception {
        super.setUp();
        cache = new FileThumbnailCache(prepareCacheDir(), 3);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File prepareCacheDir() {
        File cacheDir = new File(getInstrumentation().getTargetContext().getCacheDir(), "test-thumbnails-cache");
        FileUtils.deleteDir(cacheDir);
        cacheDir.mkdir();
        return cacheDir;
    }
}
