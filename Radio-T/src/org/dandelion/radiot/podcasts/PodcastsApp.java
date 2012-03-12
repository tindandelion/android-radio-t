package org.dandelion.radiot.podcasts;

import android.content.Context;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;
import org.dandelion.radiot.podcasts.ui.ExternalPlayer;

public class PodcastsApp {
    private static PodcastsApp instance;
    private PodcastPlayer podcastPlayer;
    
    public static void initialize(Context context) {
        instance = new PodcastsApp(context);
    }
    
    public static void release() {
        instance.releaseInstance();
        instance = null;
    }

    public static PodcastsApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(PodcastsApp newInstance) {
        instance = newInstance;
    }

    public PodcastPlayer getPodcastPlayer() {
        return podcastPlayer;
    }

    protected PodcastsApp(Context context) {
        podcastPlayer = new ExternalPlayer(context);
    }

    private void releaseInstance() {

    }
}
