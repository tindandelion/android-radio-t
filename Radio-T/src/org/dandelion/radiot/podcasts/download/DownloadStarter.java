package org.dandelion.radiot.podcasts.download;

import java.io.File;
import java.util.ArrayList;

public class DownloadStarter {
    public interface Listener {
        void onFinishedAllDownloads();
    }

    private Listener listener;
    private Downloader downloader;
    private DownloadFolder destination;
    private ArrayList<Long> downloadsInProgress;

    public DownloadStarter(Downloader downloader, DownloadFolder destination) {
        this.downloader = downloader;
        this.destination = destination;
        downloadsInProgress = new ArrayList<Long>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void downloadPodcast(String url) {
        destination.ensureExists();
        File dest = destination.makePathForUrl(url);
        long taskId = downloader.submitRequest(url, dest);
        downloadsInProgress.add(taskId);
    }

    public boolean hasDownloadsInProgress() {
        return !downloadsInProgress.isEmpty();
    }

    public void downloadCompleted(long id) {
        downloadsInProgress.remove(new Long(id));
        if (!hasDownloadsInProgress()) {
            listener.onFinishedAllDownloads();
        }
    }
}
