package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

import java.io.File;

public class RealPodcastDownloader implements PodcastProcessor {
    private PodcastDownloadManager downloadManager;
    private DownloadFolder destination;

    public RealPodcastDownloader(PodcastDownloadManager downloadManager, DownloadFolder destination) {
        this.downloadManager = downloadManager;
        this.destination = destination;
    }

    @Override
    public void process(Context context, String url) {
        destination.ensureExists();
        File dest = destination.makePathForUrl(url);
        downloadManager.submitRequest(url, dest);
    }
}
