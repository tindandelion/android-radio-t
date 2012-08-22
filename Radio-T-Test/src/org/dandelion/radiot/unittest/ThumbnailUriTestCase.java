package org.dandelion.radiot.unittest;

import junit.framework.TestCase;
import org.dandelion.radiot.podcasts.core.RssFeedModel;

public class ThumbnailUriTestCase extends TestCase {
    public void testConstructFromCompleteUrl() throws Exception {
        String url = RssFeedModel.ThumbnailUrl.construct("http://www.radio-t.com/thumbnail.jpg");
        assertEquals("http://www.radio-t.com/thumbnail.jpg", url);
    }

    public void testConstructFromRelativeUrl() throws Exception {
        String url = RssFeedModel.ThumbnailUrl.construct("/thumbnail.jpg");
        assertEquals("http://www.radio-t.com/thumbnail.jpg", url);
    }

    public void testNoUrl() throws Exception {
        assertNull(RssFeedModel.ThumbnailUrl.construct(null));
    }
}
