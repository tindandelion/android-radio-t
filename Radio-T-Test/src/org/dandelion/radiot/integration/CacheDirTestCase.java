package org.dandelion.radiot.integration;

import android.test.InstrumentationTestCase;

import java.io.File;

public class CacheDirTestCase extends InstrumentationTestCase {
    public File cacheDir() {
        return getInstrumentation().getTargetContext().getCacheDir();
    }
}
