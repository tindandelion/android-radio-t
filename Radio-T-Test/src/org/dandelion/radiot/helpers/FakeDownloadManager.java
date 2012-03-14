package org.dandelion.radiot.helpers;

import android.net.Uri;
import junit.framework.Assert;
import org.dandelion.radiot.podcasts.core.PodcastDownloadManager;

public class FakeDownloadManager implements PodcastDownloadManager {
    private SyncValueHolder<SubmitRequest> submitted = new SyncValueHolder<SubmitRequest>();

    @Override
    public void submitRequest(Uri src, Uri dest) {
        submitted.setValue(new SubmitRequest(src, dest));
    }

    public void assertSubmittedRequest(Uri src, Uri dest) {
        SubmitRequest request = submitted.getValue();
        Assert.assertEquals("Source URL", src, request.src);
        Assert.assertEquals("Destination URL", dest, request.dest);
    }


    private static class SubmitRequest {
        public Uri src;
        public Uri dest;

        public SubmitRequest(Uri src, Uri dest) {
            this.src = src;
            this.dest = dest;
        }
    }
}
