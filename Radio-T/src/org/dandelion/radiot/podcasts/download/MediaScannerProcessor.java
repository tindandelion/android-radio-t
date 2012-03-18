package org.dandelion.radiot.podcasts.download;

import java.io.File;

public class MediaScannerProcessor extends DownloadProcessor {
    private MediaScanner scanner;

    public MediaScannerProcessor(MediaScanner scanner) {
        super(null);
        this.scanner = scanner;
    }

    @Override
    public void acceptTask(DownloadTask task) {
        File path = task.localPath;
        if (path.exists()) {
            scanner.scanAudioFile(path);
        }
    }
}
