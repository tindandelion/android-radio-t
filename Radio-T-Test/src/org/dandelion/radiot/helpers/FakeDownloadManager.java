package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.DownloadTask;
import org.dandelion.radiot.podcasts.download.Downloader;

import java.io.File;

public class FakeDownloadManager implements Downloader {
    private static final long DOWNLOAD_ID = 1;
    private SyncValueHolder<SubmitRequest> submitted = new SyncValueHolder<SubmitRequest>();

    @Override
    public long submitTask(String url, DownloadTask task) {
        submitted.setValue(new SubmitRequest(url, task.localPath));
        return DOWNLOAD_ID;
    }

    public void assertSubmittedRequest(String src, File dest) {
        SubmitRequest request = submitted.getValue();
        Assert.assertEquals("Source URL", src, request.src);
        Assert.assertEquals("Destination URL", dest, request.dest);
    }


    private static class SubmitRequest {
        public String src;
        public File dest;

        public SubmitRequest(String src, File dest) {
            this.src = src;
            this.dest = dest;
        }
    }
}
