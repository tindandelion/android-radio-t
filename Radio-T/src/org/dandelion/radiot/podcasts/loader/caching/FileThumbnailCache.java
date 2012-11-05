package org.dandelion.radiot.podcasts.loader.caching;

import android.net.Uri;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileThumbnailCache implements ThumbnailCache {
    private File cacheDir;

    public FileThumbnailCache(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public void update(String url, byte[] thumbnail) {
        File cached = cachedFileForUrl(url);
        ThumbnailFile.write(cached, thumbnail);
    }

    @Override
    public byte[] lookup(String url) {
        File cached = cachedFileForUrl(url);
        return ThumbnailFile.read(cached);
    }

    private File cachedFileForUrl(String url) {
        Uri uri = Uri.parse(url);
        String fname = uri.getLastPathSegment();
        if (fname != null) {
            return new File(cacheDir, fname);
        } else {
            return null;
        }
    }

    public void cleanup(List<String> currentUrls) {
        List<File> currentFiles = cachedFilesForUrls(currentUrls);
        for (File f : redundantFiles(currentFiles)) {
            f.delete();
        }
    }

    private File[] redundantFiles(final List<File> actualFiles) {
        return cacheDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !actualFiles.contains(f);
            }
        });
    }

    private List<File> cachedFilesForUrls(List<String> urls) {
        ArrayList<File> result = new ArrayList<File>();
        for (String url : urls) {
            result.add(cachedFileForUrl(url));
        }
        return result;
    }

    private static class ThumbnailFile {

        private File file;

        public static byte[] read(File f) {
            if (f != null && f.exists()) {
                return new ThumbnailFile(f).read();
            } else {
                return null;
            }
        }

        public static void write(File f, byte[] data) {
            if (f != null) {
                new ThumbnailFile(f).write(data);
            }
        }

        private ThumbnailFile(File f) {
            this.file = f;
        }

        public byte[] read() {
            try {
                return readBytes(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private byte[] readBytes(File f) throws IOException {
            FileInputStream stream = new FileInputStream(f);
            try {
                int available = stream.available();
                byte[] buffer = new byte[available];
                stream.read(buffer, 0, available);
                return buffer;
            } finally {
                stream.close();
            }
        }

        public void write(byte[] thumbnail) {
            try {
                writeBytes(file, thumbnail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeBytes(File f, byte[] thumbnail) throws IOException {
            FileOutputStream stream = new FileOutputStream(f);
            try {
                stream.write(thumbnail, 0, thumbnail.length);
            } finally {
                stream.close();
            }
        }
    }
}
