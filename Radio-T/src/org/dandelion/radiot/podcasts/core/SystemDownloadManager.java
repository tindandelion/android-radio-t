package org.dandelion.radiot.podcasts.core;

import android.net.Uri;

public class SystemDownloadManager implements PodcastDownloadManager {
    @Override
    public void submitRequest(Uri src, Uri dest) {
        throw new RuntimeException("Not implemented");
    }
}
