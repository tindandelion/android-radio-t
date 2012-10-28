package org.dandelion.radiot.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import org.dandelion.radiot.R;
import org.dandelion.radiot.podcasts.core.ErrorListener;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastAction;

import java.net.MalformedURLException;
import java.net.URL;

class PodcastSelectionHandler implements DialogInterface.OnClickListener, AdapterView.OnItemClickListener {
    private static final int DOWNLOAD_ACTION = 0;
    private static final int PLAY_ACTION = 1;

    private Context context;
    private PodcastAction player;
    private PodcastAction downloader;
    private ErrorListener errorListener;
    private PodcastItem podcast;

    public PodcastSelectionHandler(Context context, PodcastAction player, PodcastAction downloader, ErrorListener errorListener) {
        this.context = context;
        this.player = player;
        this.downloader = downloader;
        this.errorListener = errorListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        podcast = ((PodcastVisual)parent.getAdapter().getItem(position)).podcast;
        showActionSelector(podcast.title, this);
    }

    private void showActionSelector(String title, DialogInterface.OnClickListener listener) {
        (new AlertDialog.Builder(context))
                .setTitle(title)
                .setItems(R.array.podcast_actions, listener)
                .show();
    }

    private PodcastAction selectProcessor(int index) {
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
            PodcastAction action = selectProcessor(index);
            action.perform(context, podcast);
        } catch (MalformedURLException ex) {
            errorListener.onError(context.getString(R.string.incorrect_audio_url));
        }
    }

    private void checkPodcastUrl() throws MalformedURLException {
        new URL(podcast.audioUri);
    }

}
