package org.dandelion.radiot.podcasts.download;

import java.io.File;

public class DownloadTask {
    public long id;
    public String title;
    public File localPath;
    public String url;

    public DownloadTask() {
    }

    public DownloadTask(String title, File localPath) {
        this.title = title;
        this.localPath = localPath;
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
}
