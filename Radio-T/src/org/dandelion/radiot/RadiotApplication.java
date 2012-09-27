package org.dandelion.radiot;

import android.app.Application;
import org.dandelion.radiot.podcasts.PodcastsApp;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;

import java.util.HashMap;

public class RadiotApplication extends Application {
    private HashMap<String, PodcastListLoader> engines;

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        createEngines();
    }

    protected void createEngines() {
        engines = new HashMap<String, PodcastListLoader>();
        engines.put("main-show",
                loaderForShow("main-show"));
        engines.put("after-show",
                loaderForShow("after-show"));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    protected PodcastListLoader loaderForShow(String showName) {
        return PodcastsApp.getInstance().createLoaderForShow(showName);
    }

    public PodcastListLoader getPodcastEngine(String name) {
        if (engines.containsKey(name)) {
            return engines.get(name);
        } else {
            return loaderForShow(name);
        }
    }

    public void setPodcastEngine(String name, PodcastListLoader loader) {
        engines.put(name, loader);
    }

}
