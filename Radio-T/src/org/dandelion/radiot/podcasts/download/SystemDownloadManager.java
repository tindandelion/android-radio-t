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
    public void submit(Request request) {
        android.app.DownloadManager.Request sysRequest = constructSysRequest(request);
        service.enqueue(sysRequest);
    }

    private android.app.DownloadManager.Request constructSysRequest(Request request) {
        android.app.DownloadManager.Request sysRequest = new android.app.DownloadManager.Request(Uri.parse(request.url));
        sysRequest
                .setDestinationUri(Uri.fromFile(request.localPath))
                .setTitle(request.title);
        return sysRequest;
    }

    @Override
    public CompletionInfo query(long id) {
        Cursor cursor = requestCursor(id);
        if (cursor.getCount() == 0) {
            return null;
        }
        return constructInfo(cursor);
    }

    private CompletionInfo constructInfo(Cursor cursor) {
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_TITLE));
        long status = cursor.getLong(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));

        if (isSuccessful(status)) {
            return constructSuccessfulInfo(cursor, title);
        } else {
            return constructFailureInfo(cursor, title);
        }
    }

    private CompletionInfo constructFailureInfo(Cursor cursor, String title) {
        int errorCode = cursor.getInt(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_REASON));
        return CompletionInfo.failure(title, errorCode);
    }

    private CompletionInfo constructSuccessfulInfo(Cursor cursor, String title) {
        String localPathUri = cursor.getString(cursor.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
        File localPath = new File(Uri.parse(localPathUri).getPath());
        return CompletionInfo.success(title, localPath);
    }

    private boolean isSuccessful(long status) {
        return status == android.app.DownloadManager.STATUS_SUCCESSFUL;
    }

    private Cursor requestCursor(long id) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(id);
        return service.query(query);
    }
}
