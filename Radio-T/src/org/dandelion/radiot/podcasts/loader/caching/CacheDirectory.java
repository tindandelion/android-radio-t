package org.dandelion.radiot.podcasts.loader.caching;

import java.io.File;
import java.io.FileFilter;

public class CacheDirectory {
    private final File directory;

    public CacheDirectory(File dir) {
        this.directory = dir;
    }

    public CacheDirectory(File basedir, String name) {
        this(new File(basedir, name));
    }

    public File[] listFiles(FileFilter filter) {
        File[] result = directory.listFiles(filter);
        if (result == null) {
            result = new File[] {};
        }
        return result;
    }

    public File join(String fname) {
        return new File(directory, fname);
    }
}
