package org.dandelion.radiot.podcasts.download;

import java.io.File;

public interface PodcastDownloadManager {
    void submitRequest(String src, File dest);
}
