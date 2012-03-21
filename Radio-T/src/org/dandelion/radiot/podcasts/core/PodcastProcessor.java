package org.dandelion.radiot.podcasts.core;

import android.content.Context;
import org.dandelion.radiot.podcasts.download.DownloadServiceClient;

public interface PodcastProcessor {
    public static class IncorrectPodcastAudioUrl extends Exception {
    }

	void process(Context context, PodcastItem podcast) throws IncorrectPodcastAudioUrl;
}
