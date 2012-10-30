package org.dandelion.radiot.integration;

import android.test.InstrumentationTestCase;
import org.dandelion.radiot.podcasts.loader.FileThumbnailCache;

import java.io.*;
import java.util.Arrays;

public class FileThumbnailCacheTest extends InstrumentationTestCase {
    public static final byte[] THUMBNAIL = new byte[] {0x1, 0x2, 0x3};
    private FileThumbnailCache cache;

    public void testLookupFailure() throws Exception {
        final String url = "http://example.com/thumbnail.jpg";
        assertNull(cache.lookup(url));
    }

    public void testSaveAndLoadThumbnail() throws Exception {
        final String url = "http://example.com/thumbnail.jpg";

        cache.update(url, THUMBNAIL);
        byte[] cached = cache.lookup(url);

        assertTrue(Arrays.equals(THUMBNAIL, cached));
    }

    public void testUnableToGetFilenameFromUrl() throws Exception {

    }

    protected void setUp() throws Exception {
        super.setUp();
        cache = new FileThumbnailCache(newCacheDir());
    }

    private File newCacheDir() {
        File cacheDir = new File(getInstrumentation().getTargetContext().getCacheDir(), "cache");
        deleteDir(cacheDir);
        return cacheDir;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteDir(File dir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for(File f : files) {
            f.delete();
        }
        dir.delete();
    }


}
