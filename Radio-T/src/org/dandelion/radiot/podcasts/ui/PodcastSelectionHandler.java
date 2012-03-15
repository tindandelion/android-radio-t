package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class PodcastSelectionHandler {
    private static final int DOWNLOAD_ACTION = 0;
    private static final int PLAY_ACTION = 1;

    private Context context;
    private PodcastProcessor player;
    private PodcastProcessor downloader;

    public PodcastSelectionHandler(Context context, PodcastProcessor player, PodcastProcessor downloader) {
        this.context = context;
        this.player = player;
        this.downloader = downloader;
    }

    public void podcastSelected(PodcastItem item) {
        (new AlertDialog.Builder(context))
                .setItems(R.array.podcast_actions, onClickListener(item))
                .show();
    }

    private DialogInterface.OnClickListener onClickListener(final PodcastItem item) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                switch(index) {
                    case DOWNLOAD_ACTION:
                        downloader.process(context, item.getAudioUri());
                        break;
                    case PLAY_ACTION:
                        player.process(context, item.getAudioUri());
                        break;
                }
            }
        };
    }
}
