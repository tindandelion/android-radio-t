package org.dandelion.radiot.podcasts.core;

import android.net.Uri;

public interface PodcastDownloadManager {
    void submitRequest(Uri src, Uri dest);
}
