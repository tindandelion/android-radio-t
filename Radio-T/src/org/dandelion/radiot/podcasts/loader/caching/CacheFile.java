package org.dandelion.radiot.podcasts.loader.caching;

import org.dandelion.radiot.util.ProgrammerError;

import java.io.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    private void createPathTo(File file) {
        file.getParentFile().mkdirs();
    }


    public void write(byte[] bytes) throws IOException {
        if (bytes == null) return;

        OutputStream stream = openOutputStream();
        try {
            stream.write(bytes, 0, bytes.length);
        } finally {
            stream.close();
        }
    }

    public byte[] read() throws IOException {
        if (!exists()) return null;

        InputStream stream = openInputStream();
        try {
            int available = stream.available();
            byte[] buffer = new byte[available];
            stream.read(buffer, 0, available);
            return buffer;
        } finally {
            stream.close();
        }
    }

    public boolean exists() {
        return file.exists();
    }
}
