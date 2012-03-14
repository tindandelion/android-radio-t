package org.dandelion.radiot.helpers;

import junit.framework.Assert;
import org.dandelion.radiot.podcasts.core.PodcastDownloadManager;

public class FakeDownloadManager implements PodcastDownloadManager {
    private SyncValueHolder<SubmitRequest> submitted = new SyncValueHolder<SubmitRequest>();

    @Override
    public void submitRequest(String src, String dest) {
        submitted.setValue(new SubmitRequest(src, dest));
    }

    public void assertSubmittedRequest(String src, String dest) {
        SubmitRequest request = submitted.getValue();
        Assert.assertEquals("Source URL", src, request.src);
        Assert.assertEquals("Destination URL", dest, request.dest);
    }


    private static class SubmitRequest {
        public String src;
        public String dest;

        public SubmitRequest(String src, String dest) {
            this.src = src;
            this.dest = dest;
        }
    }
}
