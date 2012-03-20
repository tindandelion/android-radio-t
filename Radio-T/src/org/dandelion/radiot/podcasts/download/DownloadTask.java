package org.dandelion.radiot.podcasts.download;

import java.io.File;

public class DownloadTask {
    public long id;
    public String title;
    public File localPath;
    public String url;
    public boolean isSuccessful;

    public DownloadTask() {
    }

    public DownloadTask(String url) {
        this.url = url;
    }

    public DownloadTask setUrl(String value) {
        url = value;
        return this;
    }

    public DownloadTask setTitle(String value) {
        title = value;
        return this;
    }

    public DownloadTask setId(long value) {
        id = value;
        return this;
    }

    public DownloadTask setLocalPath(File value) {
        localPath = value;
        return this;
    }

    public DownloadTask setSuccessful(boolean value) {
        this.isSuccessful = value;
        return this;
    }
}
