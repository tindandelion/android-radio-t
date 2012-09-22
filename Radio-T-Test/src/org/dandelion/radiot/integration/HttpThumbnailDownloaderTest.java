package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.HttpThumbnailProvider;
import org.dandelion.radiot.podcasts.core.PodcastItem;

public class HttpThumbnailDownloaderTest extends TestCase {
    private HttpThumbnailProvider downloader;

    @Override
    public void setUp() throws Exception {
        downloader = new HttpThumbnailProvider();
    }

    public void testDownloadPodcastByFullUrl() throws Exception {
        final String fullUrl = "http://www.radio-t.com/images/radio-t/rt302.jpg";
        assertNotNull(downloader.thumbnailFor(anItemWithThumbnail(fullUrl)));
    }

    public void testDownloadPodcastByPartialUrl() throws Exception {
        final String partialUrl = "/images/radio-t/rt302.jpg";
        assertNotNull(downloader.thumbnailFor(anItemWithThumbnail(partialUrl)));
    }

    public void testReturnNullIfUnableToLoadThumbnail() throws Exception {
        final String nonExistentUrl = "/non-existent/thumbnail.jpg";
        assertNull(downloader.thumbnailFor(anItemWithThumbnail(nonExistentUrl)));
    }

    public void testReturnNullIfNowUrlIsSupplied() throws Exception {
        assertNull(downloader.thumbnailFor(anItemWithThumbnail(null)));
    }

    public void testJunkUri() throws Exception {
        String junkUrl = "http://ftp://Hello world/This is junk";
        assertNull(downloader.thumbnailFor(anItemWithThumbnail(junkUrl)));
    }

    private PodcastItem anItemWithThumbnail(String url) {
        PodcastItem item = new PodcastItem();
        item.setThumbnailUrl(url);
        return item;
    }
}
