package org.dandelion.radiot.podcasts.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import android.graphics.Bitmap;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

public class RssFeedModel implements PodcastList.IModel {
    private ArrayList<PodcastItem> items;
	private PodcastItem currentItem;
	private String address;
    private ThumbnailDownloader thumbnailDownloader;

    public RssFeedModel(String address, ThumbnailDownloader thumbnailDownloader) {
        this.address = address;
        this.thumbnailDownloader = thumbnailDownloader;
    }

    public List<PodcastItem> retrievePodcasts() throws Exception {
		items = new ArrayList<PodcastItem>();
        Log.d("PODCASTS", "Starting to parse podcast list");
		Xml.parse(openContentStream(), Xml.Encoding.UTF_8, getContentHandler());
        Log.d("PODCASTS", String.format("Finished parsing with items: %d", items.size()));
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

    public Bitmap loadPodcastImage(PodcastItem item) {
        return thumbnailDownloader.loadPodcastImage(item.getThumbnailUrl());
	}
}
