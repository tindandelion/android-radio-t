package org.dandelion.radiot.podcasts.download;

import java.io.File;

public interface DownloadManager {
    long submit(DownloadTask task);
    DownloadTask query(long id);

    class DownloadTask {
        public long id;
        public String title;
        public File localPath;
        public String url;
        public boolean isSuccessful;
    }
}
