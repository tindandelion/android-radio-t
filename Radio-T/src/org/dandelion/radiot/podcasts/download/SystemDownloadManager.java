package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import android.net.Uri;

public class SystemDownloadManager implements Downloader {
    private DownloadManager manager;

    public SystemDownloadManager(DownloadManager manager) {
        this.manager = manager;
    }

    @Override
    public long submitTask(String url, DownloadTask task) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request
                .setDestinationUri(Uri.fromFile(task.localPath))
                .setTitle(task.title);
        return manager.enqueue(request);
    }
}
