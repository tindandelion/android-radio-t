package org.dandelion.radiot.podcasts.ui;

import android.content.Context;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;

public class PodcastSelectionHandler {
    private Context context;
    private PodcastPlayer player;

    public PodcastSelectionHandler(Context context, PodcastPlayer player) {
        this.context = context;
        this.player = player;
    }

    public void podcastSelected(PodcastItem item) {
        player.startPlaying(context, item.getAudioUri());
    }
}
