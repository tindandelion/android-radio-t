package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.HttpThumbnailProvider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpThumbnailProviderTest extends TestCase {
    private HttpThumbnailProvider downloader;

    @Override
    public void setUp() throws Exception {
        downloader = new HttpThumbnailProvider("http://www.radio-t.com");
    }

    public void testDownloadPodcastByFullUrl() throws Exception {
        final String fullUrl = "http://www.radio-t.com/images/radio-t/rt302.jpg";
        assertThat(downloader.thumbnailDataFor(fullUrl), is(notNullValue()));
    }

    public void testDownloadPodcastByPartialUrl() throws Exception {
        final String partialUrl = "/images/radio-t/rt302.jpg";
        assertThat(downloader.thumbnailDataFor(partialUrl), is(notNullValue()));
    }

    public void testReturnNullIfUnableToLoadThumbnail() throws Exception {
        final String nonExistentUrl = "/non-existent/thumbnail.jpg";
        assertThat(downloader.thumbnailDataFor(nonExistentUrl), is(nullValue()));
    }

    public void testReturnNullIfNowUrlIsSupplied() throws Exception {
        assertThat(downloader.thumbnailDataFor(null), is(nullValue()));
    }

    public void testJunkUri() throws Exception {
        String junkUrl = "http://ftp://Hello world/This is junk";
        assertThat(downloader.thumbnailDataFor(junkUrl), is(nullValue()));
    }
}
