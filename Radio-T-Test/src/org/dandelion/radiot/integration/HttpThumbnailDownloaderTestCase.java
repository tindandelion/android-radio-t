package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.HttpThumbnailDownloader;

public class HttpThumbnailDownloaderTestCase extends TestCase {
    private HttpThumbnailDownloader downloader;

    @Override
    public void setUp() throws Exception {
        downloader = new HttpThumbnailDownloader();
    }

    public void testDownloadPodcastByFullUrl() throws Exception {
        final String fullUrl = "http://www.radio-t.com/images/radio-t/rt302.jpg";
        assertNotNull(downloader.loadPodcastImage(fullUrl));
    }

    public void testDownloadPodcastByPartialUrl() throws Exception {
        final String partialUrl = "/images/radio-t/rt302.jpg";
        assertNotNull(downloader.loadPodcastImage(partialUrl));
    }

    public void testReturnNullIfUnableToLoadThumbnail() throws Exception {
        final String nonExistentUrl = "/non-existent/thumbnail.jpg";
        assertNull(downloader.loadPodcastImage(nonExistentUrl));
    }

    public void testReturnNullIfNowUrlIsSupplied() throws Exception {
        assertNull(downloader.loadPodcastImage(null));
    }

    public void testJunkUri() throws Exception {
        String junkUrl = "http://ftp://Hello world/This is junk";
        assertNull(downloader.loadPodcastImage(junkUrl));
    }
}
