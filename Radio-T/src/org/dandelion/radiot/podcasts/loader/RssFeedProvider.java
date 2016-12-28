package org.dandelion.radiot.podcasts.loader;

import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.dandelion.radiot.podcasts.loader.rss.HtmlFormatNotesExtractor;
import org.dandelion.radiot.podcasts.loader.rss.RssParser;

import java.io.IOException;

public class RssFeedProvider implements PodcastsProvider {
    private final String address;
    private final HttpClient client;

    public RssFeedProvider(String address) {
        this.address = address;
        this.client = new ApacheHttpClient();
    }

    public PodcastList retrieve() throws Exception {
        RssParser parser = new RssParser(new HtmlFormatNotesExtractor());
        return parser.parse(retrieveRssContent());
    }

    private String retrieveRssContent() throws IOException {
        return client.getStringContent(address);
    }

}
