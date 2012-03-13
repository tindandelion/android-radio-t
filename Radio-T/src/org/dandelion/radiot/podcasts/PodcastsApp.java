package org.dandelion.radiot.podcasts;

import org.dandelion.radiot.podcasts.core.PodcastPlayer;
import org.dandelion.radiot.podcasts.ui.ExternalPlayer;

public class PodcastsApp {
    private static PodcastsApp instance;
    private PodcastPlayer podcastPlayer;
    
    public static void initialize() {
        if (null == instance) {
            instance = new PodcastsApp();
        }
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

    protected PodcastsApp() {
        podcastPlayer = new ExternalPlayer();
    }

    private void releaseInstance() {

    }
}
