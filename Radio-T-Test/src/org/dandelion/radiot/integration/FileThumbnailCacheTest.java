package org.dandelion.radiot.integration;

import org.dandelion.radiot.podcasts.loader.caching.CacheDirectory;
import org.dandelion.radiot.podcasts.loader.caching.FileThumbnailCache;

import java.util.Arrays;
import java.util.List;

public class FileThumbnailCacheTest extends FilesystemTestCase {
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

    public void testWhenDataIsNull_DoNotStoreIt() throws Exception {
        final String url = "http://example.com/thumbnail.jpg";

        cache.update(url, null);
        assertNull(cache.lookup(url));
    }

    public void testWhenUrlIsEmpty_NeitherSaveNorLookItUp() throws Exception {
        final String url = "";

        cache.update(url, THUMBNAIL);
        assertNull(cache.lookup(url));
    }

    public void testWhenCleaningUp_RemovesThumbnailsIfNotListedInCurrentsList() throws Exception {
        cache.update("/thumbnail1.jpg", THUMBNAIL);
        cache.update("/thumbnail2.jpg", THUMBNAIL);
        cache.update("/thumbnail3.jpg", THUMBNAIL);

        List<String> currents = Arrays.asList("/thumbnail2.jpg", "/thumbnail3.jpg");
        cache.cleanup(currents);

        assertNull(cache.lookup("/thumbnail1.jpg"));
        assertNotNull(cache.lookup("/thumbnail2.jpg"));
        assertNotNull(cache.lookup("/thumbnail3.jpg"));
    }

    public void testCleaningUpAnEmptyDir_IsNotError() throws Exception {
        List<String> currents = Arrays.asList("/thumbnail2.jpg", "/thumbnail3.jpg");
        cache.cleanup(currents);
    }

    public void setUp() throws Exception {
        super.setUp();
        cache = new FileThumbnailCache(prepareCacheDir());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private CacheDirectory prepareCacheDir() {
        return new CacheDirectory(workDir);
    }
}
