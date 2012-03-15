package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;

public class FakePodcastDownloader implements PodcastDownloader {
    private Context context;

    public FakePodcastDownloader(Context context) {
        this.context = context;
    }

    @Override
    public void downloadPodcast(String url) {
        context.startActivity(new Intent(context, FakeDownloaderActivity.class));
    }
}
