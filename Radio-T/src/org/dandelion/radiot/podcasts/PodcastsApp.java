package org.dandelion.radiot.podcasts;

import org.dandelion.radiot.podcasts.core.SystemPodcastDownloader;
import org.dandelion.radiot.podcasts.core.PodcastDownloader;
import org.dandelion.radiot.podcasts.core.PodcastPlayer;
import org.dandelion.radiot.podcasts.ui.ExternalPlayer;

public class PodcastsApp {
    private static PodcastsApp instance;
    private PodcastPlayer player;
    private PodcastDownloader downloader;

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

    public PodcastPlayer getPlayer() {
        return player;
    }

    protected PodcastsApp() {
        player = new ExternalPlayer();
        downloader = new SystemPodcastDownloader();
    }

    private void releaseInstance() {

    }

    public PodcastDownloader getDownloader() {
        return downloader;
    }
}
