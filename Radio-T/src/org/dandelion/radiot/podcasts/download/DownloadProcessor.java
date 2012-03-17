package org.dandelion.radiot.podcasts.download;

public abstract class DownloadProcessor {
    private DownloadProcessor next;

    public DownloadProcessor(DownloadProcessor next) {
        this.next = next;
    }

    public abstract void acceptTask(DownloadTask task);

    protected void passFurther(DownloadTask task) {
        next.acceptTask(task);
    }
}
