package org.dandelion.radiot.podcasts.download;

public class MediaScannerProcessor extends DownloadProcessor {
    private MediaScanner scanner;

    public MediaScannerProcessor(MediaScanner scanner) {
        super(null);
        this.scanner = scanner;
    }

    @Override
    public void acceptTask(DownloadTask task) {
        scanner.scanPodcastFile(task.localPath);
    }
}
