package org.dandelion.radiot.podcasts.download;

import android.content.Context;

public interface PodcastDownloader {
    // TODO: Having context here is ugly
    void downloadPodcast(Context context, String url);
}
