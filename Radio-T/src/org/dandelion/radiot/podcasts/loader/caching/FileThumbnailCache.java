package org.dandelion.radiot.podcasts.loader.caching;

import android.net.Uri;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class FileThumbnailCache implements ThumbnailCache {
    private File cacheDir;
    private int limit;

    public FileThumbnailCache(File cacheDir, int limit) {
        this.cacheDir = cacheDir;
        this.limit = limit;
    }

    @Override
    public void update(String url, byte[] thumbnail) {
        if (willExceedLimit()) {
            removeOldestFile();
        }
        saveNewThumbnail(url, thumbnail);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void removeOldestFile() {
        File[] files = cacheDir.listFiles();
        Arrays.sort(files, new LastModifiedComparator());
        files[0].delete();
    }

    private boolean willExceedLimit() {
        return cacheDir.list().length == limit;
    }

    private void saveNewThumbnail(String url, byte[] thumbnail) {
        File cached = cachedFileForUrl(url);
        if (cached != null) {
            saveThumbnail(cached, thumbnail);
        }
    }

    @Override
    public byte[] lookup(String url) {
        File cached = cachedFileForUrl(url);
        if (cached != null && cached.exists()) {
            return loadThumbnail(cached);
        } else {
            return null;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private byte[] loadThumbnail(File cached) {
        try {
            FileInputStream stream = new FileInputStream(cached);
            int available = stream.available();
            byte[] buffer = new byte[available];
            stream.read(buffer, 0, available);
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveThumbnail(File file, byte[] thumbnail) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(thumbnail, 0, thumbnail.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static class LastModifiedComparator implements Comparator<File> {
        @Override
        public int compare(File lhs, File rhs) {
            return (int) (lhs.lastModified() - rhs.lastModified());
        }
    }
}
