package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.ErrorDisplay;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

import java.net.MalformedURLException;
import java.net.URL;

public class PodcastSelectionHandler implements DialogInterface.OnClickListener {
    private static final int DOWNLOAD_ACTION = 0;
    private static final int PLAY_ACTION = 1;

    private Context context;
    private PodcastProcessor player;
    private PodcastProcessor downloader;
    private ErrorDisplay errorDisplay;
    private PodcastItem podcast;

    public PodcastSelectionHandler(PodcastProcessor player, PodcastProcessor downloader, ErrorDisplay errorDisplay) {
        this.player = player;
        this.downloader = downloader;
        this.errorDisplay = errorDisplay;
    }

    public void process(Context context, PodcastItem podcast) {
        this.context = context;
        this.podcast = podcast;
        showActionSelector(podcast.getTitle(), this);
    }

    private void showActionSelector(String title, DialogInterface.OnClickListener listener) {
        (new AlertDialog.Builder(context))
                .setTitle(title)
                .setItems(R.array.podcast_actions, listener)
                .show();
    }

    private PodcastProcessor selectProcessor(int index) {
        switch(index) {
            case DOWNLOAD_ACTION: return downloader;
            case PLAY_ACTION: return player;
        }
        throw new RuntimeException("Unexpected action");
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int index) {
        try {
            checkPodcastUrl();
            PodcastProcessor processor = selectProcessor(index);
            processor.process(context, podcast);
        } catch (MalformedURLException ex) {
            errorDisplay.showErrorMessage(context.getString(R.string.incorrect_audio_url));
        }
    }

    private void checkPodcastUrl() throws MalformedURLException {
        new URL(podcast.getAudioUri());
    }
}
