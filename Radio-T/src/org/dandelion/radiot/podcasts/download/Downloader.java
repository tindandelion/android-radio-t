package org.dandelion.radiot.podcasts.download;

public interface Downloader {
    long submitTask(String src, DownloadTask task);
}
