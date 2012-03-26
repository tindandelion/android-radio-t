package org.dandelion.radiot.podcasts.download;

public class DownloadEngine {
    private DownloadManager downloadManager;
    private DownloadFolder destination;
    private DownloadProcessor postProcessor;

    public DownloadEngine(DownloadManager downloadManager, DownloadFolder destination, DownloadProcessor processor) {
        this.downloadManager = downloadManager;
        this.destination = destination;
        this.postProcessor = processor;
    }

    public void startDownloading(DownloadManager.Request request) {
        destination.mkdirs();
        request.localPath = destination.makePathForUrl(request.url);
        downloadManager.submit(request);
    }

    public void finishDownload(long id) {
        DownloadManager.CompletionInfo info = downloadManager.query(id);
        if (!taskCancelled(info)) {
            processCompletedTask(info);
        }
    }

    private void processCompletedTask(DownloadManager.CompletionInfo info) {
        if (info.isSuccessful) {
            postProcessor.downloadComplete(info.title, info.localPath);
        } else {
            postProcessor.downloadError(info.title, info.errorCode);
        }

    }

    private boolean taskCancelled(Object info) {
        return info == null;
    }
}

