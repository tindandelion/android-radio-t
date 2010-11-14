package org.dandelion.radiot.live;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dandelion.radiot.rss.IFeedParser;
import org.dandelion.radiot.rss.RssItem;
import org.xml.sax.SAXException;

public class LiveShowTopicsPresenter {
	private ILiveShowTopicsView view;
	private IFeedParser feedParser;

	public LiveShowTopicsPresenter(ILiveShowTopicsView view,
			IFeedParser feedParser) {
		this.view = view;
		this.feedParser = feedParser;
	}

	public void refreshTopics() {
		try {
			view.setTopics(getTopics());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<ShowTopic> getTopics() throws IOException, SAXException {
		final ArrayList<ShowTopic> result = new ArrayList<ShowTopic>();
		feedParser.parse(new IFeedParser.ParserListener() {
			public void onItemParsed(RssItem item) {
				result.add(ShowTopic.fromRss(item));
			}
		});
		return result;
	}

	public void cancelAll() {
	}
}
