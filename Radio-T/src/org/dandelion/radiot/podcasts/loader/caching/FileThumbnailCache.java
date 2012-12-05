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
        File cached = cachedFileForUrl(url);
        if (cached != null) {
            ThumbnailFile.write(new CacheFile(cached), thumbnail);
        }
    }

    @Override
    public byte[] lookup(String url) {
        File cached = cachedFileForUrl(url);
        if (cached != null) {
            return ThumbnailFile.read(new CacheFile(cached));
        } else {
            return null;
        }
    }

    private File cachedFileForUrl(String url) {
        Uri uri = Uri.parse(url);
        String fname = uri.getLastPathSegment();
        if (fname != null) {
            return cacheDir.join(fname);
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
        public static byte[] read(CacheFile f) {
            if (f.exists()) {
                try {
                    return f.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return null;
            }
        }

        public static void write(CacheFile f, byte[] data) {
            if (data != null) {
                try {
                    f.write(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
