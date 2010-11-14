package org.dandelion.radiot;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.rss.IFeedParser;
import org.dandelion.radiot.rss.RssFeedParser;
import org.dandelion.radiot.rss.RssItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RssFeedModel implements PodcastList.IModel {
	private ArrayList<PodcastItem> items;
	private IFeedParser rssParser;

	public RssFeedModel(final String feedUrl) {
		this(RssFeedParser.withRemoteFeed(feedUrl));
	}

	public RssFeedModel(IFeedParser rssParser) {
		this.rssParser = rssParser;
	}

	public List<PodcastItem> retrievePodcasts() throws Exception {
		items = new ArrayList<PodcastItem>();

		rssParser.parse(new IFeedParser.ParserListener() {
			public void onItemParsed(RssItem item) {
				items.add(PodcastItem.fromRss(item));
			}
		});
		return items;
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
