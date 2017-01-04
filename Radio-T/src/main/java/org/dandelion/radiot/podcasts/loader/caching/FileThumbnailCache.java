package org.dandelion.radiot.podcasts.loader.caching;

import android.net.Uri;
import org.dandelion.radiot.podcasts.loader.ThumbnailCache;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileThumbnailCache implements ThumbnailCache {
    private final CacheDirectory cacheDir;

    public FileThumbnailCache(CacheDirectory cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public void update(String url, byte[] thumbnail) {
        CacheFile cached = cachedFileForUrl(url);
        if (cached != null) {
            writeThumbnail(cached, thumbnail);
        }
    }

    private void writeThumbnail(CacheFile file, byte[] thumbnail) {
        try {
            file.write(thumbnail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] lookup(String url) {
        CacheFile cached = cachedFileForUrl(url);
        if (cached != null) {
            return readThumbnail(cached);
        } else {
            return null;
        }
    }

    private byte[] readThumbnail(CacheFile file) {
        try {
            return file.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CacheFile cachedFileForUrl(String url) {
        String fname = fileNameForUrl(url);
        if (fname != null) {
            return cacheDir.join(fname);
        } else {
            return null;
        }
    }

    private String fileNameForUrl(String url) {
        Uri uri = Uri.parse(url);
        return uri.getLastPathSegment();
    }

    public void cleanup(List<String> currentUrls) {
        ArrayList<String> current = new ArrayList<String>();
        for (String url : currentUrls) {
            current.add(fileNameForUrl(url));
        }
        cacheDir.deleteFilesExcluding(current);
    }
}
