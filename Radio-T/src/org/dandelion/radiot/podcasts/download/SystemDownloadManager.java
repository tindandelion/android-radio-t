package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

public class SystemDownloadManager implements DownloadManager {
    private android.app.DownloadManager service;

    public SystemDownloadManager(Context context) {
        this.service = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public long submit(DownloadTask task) {
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(task.url));
        request
                .setDestinationUri(Uri.fromFile(task.localPath))
                .setTitle(task.title);
        return service.enqueue(request);
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
        task.id = cursor.getLong(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_ID));
        task.title = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_TITLE));
        task.url = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_URI));

        long status = cursor.getLong(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
        task.isSuccessful = (status == android.app.DownloadManager.STATUS_SUCCESSFUL);
        if (!task.isSuccessful) {
            task.errorCode = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_REASON));
        }

        String localPathUri = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
        if (localPathUri != null) {
            task.localPath = new File(Uri.parse(localPathUri).getPath());
        }
        return task;
    }

    private Cursor requestCursor(long id) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(id);
        return service.query(query);
    }
}
