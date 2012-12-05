package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.util.ProgrammerError;

import java.io.*;

public class CacheFile {
    protected final File file;

    public CacheFile(File dir, String fname) {
        this(new File(dir, fname));
    }

    public CacheFile(File file) {
        this.file = file;
    }

    public long age() {
        return System.currentTimeMillis() - file.lastModified();
    }

    public InputStream openInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete() {
        file.delete();
    }

    public OutputStream openOutputStream() {
        try {
            createPathTo(file);
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new ProgrammerError(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createPathTo(File file) {
        file.getParentFile().mkdirs();
    }
}
