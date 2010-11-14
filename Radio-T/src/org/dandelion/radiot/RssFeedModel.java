package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.rss.RssEnclosure;
import org.dandelion.radiot.rss.RssFeedParser;
import org.dandelion.radiot.rss.RssItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RssFeedModel implements PodcastList.IModel {
	private ArrayList<PodcastItem> items;
	private String address;

	public RssFeedModel(String url) {
		this.address = url;
	}

	public List<PodcastItem> retrievePodcasts() throws Exception {
		items = new ArrayList<PodcastItem>();
		RssFeedParser parser = createFeedParser();

		parser.setItemListener(new RssFeedParser.FeedItemListener() {
			public void item(RssItem item) {
				PodcastItem currentItem = new PodcastItem();
				currentItem.extractPodcastNumber(item.title);
				currentItem.extractPubDate(item.pubDate);
				currentItem.extractShowNotes(item.description);
				currentItem.extractImageUrl(item.encodedContent);

				for (String category : item.categories) {
					currentItem.addTag(category);
				}

				for (RssEnclosure enclosure : item.getEnclosures("audio/mpeg")) {
					currentItem.extractAudioUri(enclosure.url);
				}
				items.add(currentItem);
				currentItem = new PodcastItem();
			}
		});

		parser.parse();
		return items;
	}

	protected RssFeedParser createFeedParser() throws IOException {
		return new RssFeedParser(openContentStream());
	}

	protected InputStream openContentStream() throws IOException {
		URL url = new URL(address);
		return url.openStream();
	}

	protected InputStream openImageStream(String address) {
		try {
			URL url = new URL(address);
			return url.openStream();
		} catch (Exception ex) {
			return null;
		}
	}

	public Bitmap loadPodcastImage(PodcastItem item) {
		return BitmapFactory.decodeStream(openImageStream(item.getImageUrl()));
	}
}
