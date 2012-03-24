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

    public void startDownloading(DownloadManager.DownloadTask task) {
        destination.mkdirs();
        task.localPath = destination.makePathForUrl(task.url);
        downloadManager.submit(task);
    }

    public void finishDownload(long id) {
        DownloadManager.DownloadTask completedTask = downloadManager.query(id);
        if (!taskCancelled(completedTask)) {
            processCompletedTask(completedTask);
        }
    }

    private void processCompletedTask(DownloadManager.DownloadTask task) {
        if (task.isSuccessful) {
            postProcessor.downloadComplete(task.title, task.localPath);
        } else {
            postProcessor.downloadError(task.title, task.errorCode);
        }

    }

    private boolean taskCancelled(DownloadManager.DownloadTask task) {
        return task == null;
    }
}

