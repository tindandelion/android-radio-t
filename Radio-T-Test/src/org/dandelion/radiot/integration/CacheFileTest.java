package org.dandelion.radiot.integration;

import org.dandelion.radiot.integration.helpers.FileUtils;
import org.dandelion.radiot.podcasts.loader.caching.CacheFile;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;

public class CacheFileTest extends FilesystemTestCase {
    public void testWhenWritingFile_CreatesDirectoryStructure() throws Exception {
        File subdir = new File(cacheDir(), "subdir");
        CacheFile cacheFile = new CacheFile(subdir, "cache-file");

        FileUtils.deleteDir(subdir);
        cacheFile.openOutputStream().close();

        assertThat(subdir, exists());
    }

    private Matcher<? super File> exists() {
        return new TypeSafeMatcher<File>() {
            @Override
            protected boolean matchesSafely(File file) {
                return file.exists();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("filesystem object exists");
            }
        };
    }
}
