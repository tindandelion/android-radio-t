package org.dandelion.radiot.integration;

import android.net.Uri;
import junit.framework.TestCase;

public class UriTest extends TestCase {
    public void testGettingLastPathSegment() throws Exception {
        Uri uri = Uri.parse("http://example.com/filename.txt");
        assertEquals("filename.txt", uri.getLastPathSegment());
    }
}
