package org.dandelion.radiot.podcasts.loader;

import android.sax.*;
import android.util.Xml;
import org.dandelion.radiot.http.ApacheHttpClient;
import org.dandelion.radiot.http.HttpClient;
import org.dandelion.radiot.podcasts.core.PodcastItem;
import org.dandelion.radiot.podcasts.core.PodcastList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import java.io.IOException;

public class RssFeedProvider implements PodcastsProvider {
    private PodcastList items;
	private PodcastItem currentItem;
	private String address;
    private final HttpClient client;

    public RssFeedProvider(String address) {
        this(address, new ApacheHttpClient());
    }

    public RssFeedProvider(String address, HttpClient client) {
        this.address = address;
        this.client = client;
    }

    public PodcastList retrieve() throws Exception {
		items = new PodcastList();
        Xml.parse(retrieveRssContent(), contentHandler());
		return items;
	}

    protected String retrieveRssContent() throws IOException {
        return client.getStringContent(address);
    }

    private ContentHandler contentHandler() {
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");
		currentItem = new PodcastItem();

		item.setEndElementListener(new EndElementListener() {
			public void end() {
				items.add(currentItem);
				currentItem = new PodcastItem();
			}
		});

		item.getChild("title").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
                        currentItem.title = body;
                    }
				});

		item.getChild("pubDate").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.pubDate = body;
					}
				});

        item.getChild("description").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                currentItem.extractThumbnailUrl(body);
            }
        });

		item.getChild("enclosure").setStartElementListener(
				new StartElementListener() {
					public void start(Attributes attributes) {
						if (isAudioEnclosure(attributes)) {
                            currentItem.audioUri = attributes.getValue("url");
                        }
					}

					private boolean isAudioEnclosure(Attributes attributes) {
						return attributes.getValue("type").equals("audio/mpeg");
					}
				});

        item.getChild("http://www.itunes.com/dtds/podcast-1.0.dtd", "summary")
                .setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        currentItem.showNotes = s;
                    }
                });

		return root.getContentHandler();
	}

}
