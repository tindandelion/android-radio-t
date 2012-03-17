package org.dandelion.radiot.podcasts.download;

import android.content.Context;
import android.content.Intent;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastProcessor;

public class FakePodcastDownloader implements PodcastProcessor {
    @Override
    public void process(Context context, PodcastItem podcast) {
        context.startActivity(new Intent(context, FakeDownloaderActivity.class));
    }
}
