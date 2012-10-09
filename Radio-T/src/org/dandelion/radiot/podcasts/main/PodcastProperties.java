package org.dandelion.radiot.podcasts.main;

import java.util.HashMap;

public class PodcastProperties {
    private static HashMap<String, PodcastProperties> shows;

    public String name;
    public String url;

    static {
        shows = new HashMap<String, PodcastProperties>();
        shows.put("main-show",
                new PodcastProperties("main-show", "http://feeds.rucast.net/radio-t"));
        shows.put("after-show",
                new PodcastProperties("after-show", "http://feeds.feedburner.com/pirate-radio-t"));
    }

    public static PodcastProperties propertiesForShow(String name) {
        return shows.get(name);
    }

    private PodcastProperties(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
