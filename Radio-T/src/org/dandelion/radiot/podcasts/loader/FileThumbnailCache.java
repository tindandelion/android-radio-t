package org.dandelion.radiot.podcasts.loader;

import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileThumbnailCache implements ThumbnailCache {
    private File cacheDir;

    public FileThumbnailCache(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public void update(String url, byte[] thumbnail) {
        makeCacheDir();
        File cached = cachedFileForUrl(url);
        saveThumbnail(cached, thumbnail);
    }

    @Override
    public byte[] lookup(String url) {
        File cached = cachedFileForUrl(url);
        if (cached.exists()) {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void makeCacheDir() {
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
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
        return new File(cacheDir, uri.getLastPathSegment());
    }

}
