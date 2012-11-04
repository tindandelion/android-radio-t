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
}
