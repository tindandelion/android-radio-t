package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import org.dandelion.radiot.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.dandelion.radiot.util.IconNote;

import java.io.File;

public class DownloadNotifier implements NotificationManager {
    private int DOWNLOAD_COMPLETE_NOTE_ID = 2;
    private Context context;
    private DownloadErrorMessages errorMessages;

    public DownloadNotifier(Context context, DownloadErrorMessages messages) {
        this.context = context;
        this.errorMessages = messages;
    }

    @Override
    public void showSuccess(String title, File audioFile) {
        String text = context.getString(R.string.download_complete_message);
        new SuccessNote(context, DOWNLOAD_COMPLETE_NOTE_ID, audioFile)
                .setTitleAndTicker(title)
                .setText(text)
                .setIcon(R.drawable.stat_download_complete)
                .show(title);
    }

    @Override
    public void showError(String title, int errorCode) {
        String text = errorMessages.getMessageForCode(errorCode);
        new ErrorNote(context, DOWNLOAD_COMPLETE_NOTE_ID)
                .setTitleAndTicker(title)
                .setText(text)
                .setIcon(android.R.drawable.stat_sys_warning)
                .show(title);
    }
    class ErrorNote extends IconNote {

        public ErrorNote(Context context, int notificationId) {
            super(context, notificationId);
        }

        @Override
        protected Intent activityIntent() {
            return new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        }
    }

    class SuccessNote extends IconNote {
        private File path;

        SuccessNote(Context context, int notificationId, File path) {
            super(context, notificationId);
            this.path = path;
        }

        @Override
        protected Intent activityIntent() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(path), "audio/mpeg");
            return intent;
        }
    }
}


