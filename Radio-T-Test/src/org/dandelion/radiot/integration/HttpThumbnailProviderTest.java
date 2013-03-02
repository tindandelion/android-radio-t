package org.dandelion.radiot.integration;

import junit.framework.TestCase;
import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.podcasts.loader.HttpThumbnailProvider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpThumbnailProviderTest extends TestCase {
    private HttpThumbnailProvider thumbnails;

    @Override
    public void setUp() throws Exception {
        thumbnails = new HttpThumbnailProvider(new ApacheHttpClient());
    }

    public void testDownloadPodcastByFullUrl() throws Exception {
        final String fullUrl = "http://www.radio-t.com/images/radio-t/rt302.jpg";
        assertThat(thumbnails.thumbnailDataFor(fullUrl), is(notNullValue()));
    }

    public void testReturnNullIfUnableToLoadThumbnail() throws Exception {
        final String nonExistentUrl = "http://www.radio-t.com/non-existent/thumbnail.jpg";
        assertThat(thumbnails.thumbnailDataFor(nonExistentUrl), is(nullValue()));
    }

    public void testJunkUri() throws Exception {
        String junkUrl = "http://ftp://Hello world/This is junk";
        assertThat(thumbnails.thumbnailDataFor(junkUrl), is(nullValue()));
    }
}
