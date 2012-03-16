package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import android.net.Uri;

import java.io.File;

public class SystemDownloadManager implements PodcastDownloadManager {
    private DownloadManager manager;

    public SystemDownloadManager(DownloadManager manager) {
        this.manager = manager;
    }

    @Override
    public void submitRequest(String src, File dest) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(src));
        request.setDestinationUri(Uri.fromFile(dest));
        manager.enqueue(request);
    }
}
