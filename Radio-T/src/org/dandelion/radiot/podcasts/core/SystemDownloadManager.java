package org.dandelion.radiot.podcasts.core;

import android.app.DownloadManager;
import android.net.Uri;

import java.io.File;

public class SystemDownloadManager implements PodcastDownloadManager {
    private DownloadManager manager;

    public SystemDownloadManager(DownloadManager manager) {
        this.manager = manager;
    }

    @Override
    public void submitRequest(String src, String dest) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(src));
        request.setDestinationUri(Uri.fromFile(new File(dest)));
        manager.enqueue(request);
    }
}
