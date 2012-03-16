package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.PodcastDownloadManager;

import java.io.File;

public class FakeDownloadManager implements PodcastDownloadManager {
    private SyncValueHolder<SubmitRequest> submitted = new SyncValueHolder<SubmitRequest>();

    @Override
    public void submitRequest(String src, File dest) {
        submitted.setValue(new SubmitRequest(src, dest));
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
