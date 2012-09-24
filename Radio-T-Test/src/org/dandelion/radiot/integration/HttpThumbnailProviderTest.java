package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.HttpThumbnailProvider;

public class HttpThumbnailProviderTest extends TestCase {
    private HttpThumbnailProvider downloader;

    @Override
    public void setUp() throws Exception {
        downloader = new HttpThumbnailProvider("http://www.radio-t.com");
    }

    public void testDownloadPodcastByFullUrl() throws Exception {
        final String fullUrl = "http://www.radio-t.com/images/radio-t/rt302.jpg";
        assertNotNull(downloader.thumbnailDataFor(fullUrl));
    }

    public void testDownloadPodcastByPartialUrl() throws Exception {
        final String partialUrl = "/images/radio-t/rt302.jpg";
        assertNotNull(downloader.thumbnailDataFor(partialUrl));
    }

    public void testReturnNullIfUnableToLoadThumbnail() throws Exception {
        final String nonExistentUrl = "/non-existent/thumbnail.jpg";
        assertNull(downloader.thumbnailDataFor(nonExistentUrl));
    }

    public void testReturnNullIfNowUrlIsSupplied() throws Exception {
        assertNull(downloader.thumbnailDataFor(null));
    }

    public void testJunkUri() throws Exception {
        String junkUrl = "http://ftp://Hello world/This is junk";
        assertNull(downloader.thumbnailDataFor(junkUrl));
    }

}
