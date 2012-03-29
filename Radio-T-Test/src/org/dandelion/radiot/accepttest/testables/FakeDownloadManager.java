package org.dandelion.radiot.accepttest.testables;

import android.content.Context;
import android.content.Intent;
import junit.framework.Assert;
import org.dandelion.radiot.helpers.SyncValueHolder;
import org.dandelion.radiot.podcasts.download.DownloadManager;

import java.io.File;
import java.io.IOException;

public class FakeDownloadManager implements DownloadManager {
    public static final long TASK_ID = 1;
    private SyncValueHolder<Request> requestHolder = new SyncValueHolder<Request>();
    private CompletionInfo completionInfo;
    private Context context;

    public FakeDownloadManager(Context context) {
        this.context = context;
    }

    @Override
    public void submit(Request request) {
        requestHolder.setValue(request);
    }

    @Override
    public CompletionInfo query(long id) {
        return completionInfo;
    }

    public void cancelDownload() {
        requestHolder.waitForValue();
        completionInfo = null;
        sendCompletionBroadcast();
    }

    public void downloadComplete() {
        finishDownload(true, 0);
    }

    public void downloadAborted(int errorCode) {
        finishDownload(false, errorCode);
    }

    private void finishDownload(boolean success, int errorCode) {
        Request request = requestHolder.getValue();
        if (success) {
            completionInfo = CompletionInfo.success(request.title, request.localPath);
            createEmptyFile(completionInfo.localPath);
        } else {
            completionInfo = CompletionInfo.failure(request.title, errorCode);
        }
        sendCompletionBroadcast();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createEmptyFile(File path) {
        try {
            path.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCompletionBroadcast() {
        Intent intent = new Intent(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intent.putExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, FakeDownloadManager.TASK_ID);
        context.sendBroadcast(intent);
    }

    public void assertSubmittedRequest(String src, File dest) {
        Request request = this.requestHolder.getValue();
        Assert.assertEquals("Source URL", src, request.url);
        Assert.assertEquals("Destination URL", dest, request.localPath);
    }
}
