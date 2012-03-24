package org.dandelion.radiot.helpers;

import android.content.Context;
import android.content.Intent;
import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.DownloadManager;

import java.io.File;
import java.io.IOException;

public class FakeDownloadManager implements DownloadManager {
    public static final long TASK_ID = 1;
    private SyncValueHolder<DownloadTask> submitted = new SyncValueHolder<DownloadTask>();
    private Context context;

    public FakeDownloadManager(Context context) {
        this.context = context;
    }

    @Override
    public long submit(DownloadTask task) {
        task.id = TASK_ID;
        submitted.setValue(task);
        return TASK_ID;
    }

    @Override
    public DownloadTask query(long id) {
        return submitted.getValue();
    }

    public void cancelDownload() {
        submitted.waitForValue();
        submitted.setValue(null);
        sendCompletionBroadcast();
    }

    public void downloadComplete() {
        finishDownload(true, 0);
    }

    public void downloadAborted(int errorCode) {
        finishDownload(false, errorCode);
    }

    private void finishDownload(boolean success, int errorCode) {
        DownloadTask value = submitted.getValue();
        value.isSuccessful = success;
        value.errorCode = errorCode;
        if (success) {
            createEmptyFile(value);
        }
        sendCompletionBroadcast();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createEmptyFile(DownloadTask value) {
        try {
            value.localPath.createNewFile();
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
        DownloadTask request = submitted.getValue();
        Assert.assertEquals("Source URL", src, request.url);
        Assert.assertEquals("Destination URL", dest, request.localPath);
    }
}
