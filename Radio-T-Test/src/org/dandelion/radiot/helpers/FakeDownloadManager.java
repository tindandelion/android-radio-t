package org.dandelion.radiot.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import junit.framework.Assert;
import org.dandelion.radiot.podcasts.download.DownloadTask;
import org.dandelion.radiot.podcasts.download.Downloader;

import java.io.File;
import java.io.IOException;

public class FakeDownloadManager implements Downloader {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void downloadComplete() {
        DownloadTask value = submitted.getValue();
        try {
            value.localPath.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendCompletionBroadcast();
    }

    private void sendCompletionBroadcast() {
        Intent intent = new Intent(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, FakeDownloadManager.TASK_ID);
        context.sendBroadcast(intent);
    }

    public void assertSubmittedRequest(String src, File dest) {
        DownloadTask request = submitted.getValue();
        Assert.assertEquals("Source URL", src, request.url);
        Assert.assertEquals("Destination URL", dest, request.localPath);
    }
}
