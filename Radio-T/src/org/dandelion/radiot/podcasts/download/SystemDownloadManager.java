package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

public class SystemDownloadManager implements Downloader {
    private DownloadManager manager;

    public SystemDownloadManager(DownloadManager manager) {
        this.manager = manager;
    }

    @Override
    public long submit(DownloadTask task) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(task.url));
        request
                .setDestinationUri(Uri.fromFile(task.localPath))
                .setTitle(task.title);
        return manager.enqueue(request);
    }

    @Override
    public DownloadTask query(long id) {
        Cursor cursor = requestCursor(id);
        if (cursor.getCount() == 0) {
            return null;
        }
        return constructTask(cursor);
    }

    private DownloadTask constructTask(Cursor cursor) {
        DownloadTask task = new DownloadTask();
        cursor.moveToFirst();
        task.id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
        task.title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
        task.url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));

        long status = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        task.isSuccessful = (status == DownloadManager.STATUS_SUCCESSFUL);

        String localPathUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        task.localPath = new File(Uri.parse(localPathUri).getPath());

        return task;
    }

    private Cursor requestCursor(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        return manager.query(query);
    }
}
