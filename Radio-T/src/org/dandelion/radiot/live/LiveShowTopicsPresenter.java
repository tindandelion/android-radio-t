package org.dandelion.radiot.live;

import org.dandelion.radiot.rss.IFeedParser;
import org.dandelion.radiot.rss.RssItem;

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
			view.clearTopics();
			feedParser.parse(new IFeedParser.ParserListener() {
				public void onItemParsed(RssItem item) {
					view.addTopic(ShowTopic.fromRss(item));
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
