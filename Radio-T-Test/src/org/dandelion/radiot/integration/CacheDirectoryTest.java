package org.dandelion.radiot.integration;

import org.dandelion.radiot.integration.helpers.FileUtils;
import org.dandelion.radiot.podcasts.loader.caching.CacheDirectory;

import java.io.File;
import java.io.FileFilter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CacheDirectoryTest extends FilesystemTestCase {
    public void testWhenListingFilesInAbsentDirectory_ReturnsEmptyList() throws Exception {
        File subdir = new File(cacheDir(), "subdir");
        CacheDirectory cacheDir = new CacheDirectory(subdir);

        FileUtils.deleteDir(subdir);
        File[] files = cacheDir.listFiles(allFiles());

        assertThat(files, notNullValue());
        assertThat(files.length, equalTo(0));

    }

    public void testConstructChildName() throws Exception {
        File subdir = new File(cacheDir(), "subdir");
        CacheDirectory cacheDir = new CacheDirectory(subdir);

        assertThat(cacheDir.join("filename"), equalTo(new File(subdir, "filename")));
    }

    private FileFilter allFiles() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };
    }
}
