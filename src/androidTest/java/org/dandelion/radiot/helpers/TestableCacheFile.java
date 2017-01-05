package org.dandelion.radiot.helpers;

import org.dandelion.radiot.podcasts.loader.caching.CacheFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestableCacheFile extends CacheFile {
    public TestableCacheFile(File dir, String s) {
        super(dir, s);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setLastModified(long timestamp) {
        file.setLastModified(timestamp);
    }

    public void write(String content) throws IOException {
        FileWriter out = new FileWriter(file);
        out.write(content);
    }
}
