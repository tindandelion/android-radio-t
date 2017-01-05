package org.dandelion.radiot.podcasts.download;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFolder {
    private File path;

    public DownloadFolder(File path) {
        this.path = path;
    }

    public File makePathForUrl(String srcUrl) {
        try {
            File pathFragment = new File(new URL(srcUrl).getPath());
            return new File(path, pathFragment.getName());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void mkdirs() {
        path.mkdirs();
    }
}
