package org.dandelion.radiot.integration;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilePodcastsCacheTest extends InstrumentationTestCase {
    private File cacheDir;

    public void testGettingCacheDir() throws Exception {
        assertThat(cacheDir, is(notNullValue()));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getTargetContext();
        cacheDir = context.getCacheDir();
    }
}
