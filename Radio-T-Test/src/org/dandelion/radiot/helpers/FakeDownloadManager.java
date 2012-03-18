package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.DownloadTask;
import org.dandelion.radiot.podcasts.download.Downloader;

import java.io.File;

public class FakeDownloadManager implements Downloader {
    public static final long TASK_ID = 1;
    private SyncValueHolder<DownloadTask> submitted = new SyncValueHolder<DownloadTask>();

    @Override
    public long submit(DownloadTask task) {
        submitted.setValue(task);
        return TASK_ID;
    }

    public void assertSubmittedRequest(String src, File dest) {
        DownloadTask request = submitted.getValue();
        Assert.assertEquals("Source URL", src, request.url);
        Assert.assertEquals("Destination URL", dest, request.localPath);
    }
}
