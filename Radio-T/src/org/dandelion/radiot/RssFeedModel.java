package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
				items.add(PodcastItem.fromRss(item));
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
