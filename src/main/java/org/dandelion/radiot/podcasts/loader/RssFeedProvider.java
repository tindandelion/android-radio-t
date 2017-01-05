package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.http.OkBasedHttpClient;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.rss.HtmlFormatNotesExtractor;
import org.dandelion.radiot.podcasts.loader.rss.RssParser;

import java.io.IOException;

public class RssFeedProvider implements PodcastsProvider {
    private final HttpClient client = OkBasedHttpClient.make();
    private final String address;

    public RssFeedProvider(String address) {
        this.address = address;
    }

    public PodcastList retrieve() throws Exception {
        RssParser parser = new RssParser(new HtmlFormatNotesExtractor());
        return parser.parse(retrieveRssContent());
    }

    private String retrieveRssContent() throws IOException {
        return client.getStringContent(address);
    }

}
