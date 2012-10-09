package org.dandelion.radiot;

import android.app.Application;
import org.dandelion.radiot.podcasts.main.PodcastsApp;

public class RadiotApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }
}
