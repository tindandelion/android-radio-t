package org.dandelion.radiot.podcasts.download;

import android.app.DownloadManager;
import org.dandelion.radiot.R;
import android.content.Context;
import android.net.Uri;
import org.dandelion.radiot.util.IconNote;

import java.io.File;

public class DownloadNotifier implements NotificationManager {
    private int DOWNLOAD_COMPLETE_NOTE_ID = 10;
    private Context context;
    private DownloadErrorMessages errorMessages;

    public DownloadNotifier(Context context, DownloadErrorMessages messages) {
        this.context = context;
        this.errorMessages = messages;
    }

    @Override
    public void showSuccess(final String title, final File audioFile) {
        final String text = context.getString(R.string.download_complete_message);

        new IconNote(context, DOWNLOAD_COMPLETE_NOTE_ID) {{
            setTitleAndTicker(title);
            setText(text);
            setIcon(R.drawable.stat_download_complete);
            opensUri(Uri.fromFile(audioFile), "audio/mpeg");
            show(title);
        }};
    }

    @Override
    public void showError(final String title, int errorCode) {
        final String text = errorMessages.getMessageForCode(errorCode);

        new IconNote(context, DOWNLOAD_COMPLETE_NOTE_ID) {{
            setTitleAndTicker(title);
            setText(text);
            setIcon(android.R.drawable.stat_sys_warning);
            performsAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
            show(title);
        }};
    }
}


