package org.dandelion.radiot.podcasts.core;

import android.sax.*;
import android.util.Xml;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssFeedProvider implements PodcastsProvider {
    private ArrayList<PodcastItem> items;
	private PodcastItem currentItem;
	private String address;

    public RssFeedProvider(String address) {
        this.address = address;
    }

    public List<PodcastItem> retrieveAll() throws Exception {
		items = new ArrayList<PodcastItem>();
		Xml.parse(openContentStream(), Xml.Encoding.UTF_8, getContentHandler());
		return items;
	}

	protected InputStream openContentStream() throws IOException {
		URL url = new URL(address);
		return url.openStream();
	}

	private ContentHandler getContentHandler() {
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
						currentItem.setTitle(body);
					}
				});

		item.getChild("pubDate").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.extractPubDate(body);
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
                            currentItem.setAudioUri(attributes
                                    .getValue("url"));
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
                        currentItem.extractShowNotes(s);
                    }
                });

		return root.getContentHandler();
	}

}
