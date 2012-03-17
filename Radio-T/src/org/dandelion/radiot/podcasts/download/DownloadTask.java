package org.dandelion.radiot.podcasts.download;

import java.io.File;

public class DownloadTask {
    public String title;
    public File localPath;

    public DownloadTask(String title, File localPath) {
        this.title = title;
        this.localPath = localPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DownloadTask) {
            DownloadTask other = (DownloadTask) o;
            return title.equals(other.title) &&
                    localPath.equals(other.localPath);
        }
        return false;
    }
}
