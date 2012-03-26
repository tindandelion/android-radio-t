package org.dandelion.radiot.podcasts.download;

import java.io.File;

public interface DownloadManager {
    void submit(Request request);
    CompletionInfo query(long id);

    class DownloadTask {
        public final String title;
        public File localPath;

        public DownloadTask(String title) {
            this.title = title;
        }
    }

    class Request extends DownloadTask {
        public final String url;
        public Request(String url, String title) {
            super(title);
            this.url = url;
        }
    }

    class CompletionInfo extends DownloadTask {
        public boolean isSuccessful;
        public int errorCode;

        public CompletionInfo(String title) {
            super(title);
        }

        public static CompletionInfo success(String title, File path) {
            CompletionInfo value = new CompletionInfo(title);
            value.isSuccessful = true;
            value.localPath = path;
            return value;
        }

        public static CompletionInfo failure(String title, int errorCode) {
            CompletionInfo value = new CompletionInfo(title);
            value.isSuccessful = false;
            value.errorCode = errorCode;
            return value;
        }
    }
}
