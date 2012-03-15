package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;

public class FakePodcastDownloader implements PodcastDownloader {
    @Override
    public void downloadPodcast(Context context, String url) {
        context.startActivity(new Intent(context, FakeDownloaderActivity.class));
    }
}
