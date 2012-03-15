package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class PodcastSelectionHandler implements PodcastProcessor {
    private static final int DOWNLOAD_ACTION = 0;
    private static final int PLAY_ACTION = 1;

    private Context context;
    private PodcastProcessor player;
    private PodcastProcessor downloader;

    public PodcastSelectionHandler(PodcastProcessor player, PodcastProcessor downloader) {
        this.player = player;
        this.downloader = downloader;
    }

    @Override
    public void process(Context context, String url) {
        this.context = context;
        showActionSelector(onClickListener(url));
    }

    private void showActionSelector(DialogInterface.OnClickListener listener) {
        (new AlertDialog.Builder(context))
                .setItems(R.array.podcast_actions, listener)
                .show();
    }

    private DialogInterface.OnClickListener onClickListener(final String url) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                PodcastProcessor processor = selectProcessor(index);
                processor.process(context, url);
            }
        };
    }

    private PodcastProcessor selectProcessor(int index) {
        switch(index) {
            case DOWNLOAD_ACTION: return downloader;
            case PLAY_ACTION: return player;
        }
        throw new RuntimeException("Unexpected action");
    }

}
