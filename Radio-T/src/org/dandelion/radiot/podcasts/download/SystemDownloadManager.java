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
        cursor.moveToFirst();
        return new DownloadTask()
                .setId(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)))
                .setLocalPath(new File(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))))
                .setTitle(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)))
                .setUrl(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI)));
    }

    private Cursor requestCursor(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        return manager.query(query);
    }
}
