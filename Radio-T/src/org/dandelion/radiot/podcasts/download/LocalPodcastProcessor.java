package org.dandelion.radiot.podcasts.download;

public class LocalPodcastProcessor implements DownloadTracker.PostProcessor {
    private MediaScanner scanner;

    public LocalPodcastProcessor(MediaScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void downloadComplete(DownloadTask task) {
        scanner.scanFile(task.localPath);
    }
}
