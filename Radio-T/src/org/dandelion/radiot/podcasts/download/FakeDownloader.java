package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastAction;

public class FakeDownloader implements PodcastAction {
    @Override
    public void perform(Context context, PodcastItem podcast) {
        context.startActivity(new Intent(context, FakeDownloaderActivity.class));
    }
}
