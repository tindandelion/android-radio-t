package org.dandelion.radiot.podcasts.download;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class PodcastDownloader {
    private PodcastDownloadManager downloadManager;
    private File destFolder;

    public PodcastDownloader(PodcastDownloadManager downloadManager, File destFolder) {
        this.downloadManager = downloadManager;
        this.destFolder = destFolder;
    }

    public void downloadPodcast(String url) {
        ensureDestFolderExists();
        String destPath = createDestPath(url);
        downloadManager.submitRequest(url, destPath);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void ensureDestFolderExists() {
        destFolder.mkdirs();
    }

    private String createDestPath(String source) {
        try {
            String destFilename = new URL(source).getFile();
            return new File(destFolder.toString(), destFilename).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
