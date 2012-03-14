package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.PodcastDownloader;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

public class PodcastSelectionHandler {
    private static final int DOWNLOAD_ACTION = 0;
    private static final int PLAY_ACTION = 1;

    private Context context;
    private PodcastPlayer player;
    private PodcastDownloader downloader;

    public PodcastSelectionHandler(Context context, PodcastPlayer player, PodcastDownloader downloader) {
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
                        downloader.downloadPodcast(item.getAudioUri());
                        break;
                    case PLAY_ACTION:
                        player.startPlaying(context, item.getAudioUri());
                        break;
                }
            }
        };
    }
}
