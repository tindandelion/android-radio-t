package org.dandelion.radiot.podcasts.loader.caching;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class CacheDirectory {
    private final File directory;

    public CacheDirectory(File dir) {
        this.directory = dir;
    }

    public CacheDirectory(File basedir, String name) {
        this(new File(basedir, name));
    }

    public CacheFile join(String fname) {
        return new CacheFile(directory, fname);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteFilesExcluding(ArrayList<String> current) {
        for (File f : listFilesExcluding(current)) {
            f.delete();
        }
    }

    private File[] listFilesExcluding(final ArrayList<String> exclusions) {
        File[] result = directory.listFiles(new ExclusionFilter(exclusions));
        if (result == null) {
            result = new File[] {};
        }
        return result;
    }

    private static class ExclusionFilter implements FileFilter {
        private final ArrayList<String> exclusions;

        public ExclusionFilter(ArrayList<String> exclusions) {
            this.exclusions = exclusions;
        }

        @Override
        public boolean accept(File f) {
            return !exclusions.contains(f.getName());
        }
    }
}
