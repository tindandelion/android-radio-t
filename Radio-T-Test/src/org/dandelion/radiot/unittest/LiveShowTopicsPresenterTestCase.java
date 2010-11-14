package org.dandelion.radiot.unittest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.live.ILiveShowTopicsView;
import org.dandelion.radiot.live.LiveShowTopicsPresenter;
import org.dandelion.radiot.live.ShowTopic;
import org.dandelion.radiot.rss.AbstractFeedParser;
import org.dandelion.radiot.rss.RssItem;
import org.xml.sax.SAXException;

class FakeRssFeed extends AbstractFeedParser {
	List<RssItem> items = new ArrayList<RssItem>();
	
	public FakeRssFeed() {
		super(null);
	}
	
	@Override
	public void parse(ParserListener listener) throws IOException, SAXException {
		for (RssItem item : items) {
			listener.onItemParsed(item);
		}
	}

	public void addItem(RssItem rssItem) {
		items.add(rssItem);
	}
}

public class LiveShowTopicsPresenterTestCase extends TestCase implements ILiveShowTopicsView {
	
	private List<ShowTopic> topics;

	public void testCreateShowTopicListFromRss() throws Exception {
		FakeRssFeed rssFeed = new FakeRssFeed();
		RssItem rssItem = new RssItem();
		rssItem.title = "Live show topic";
		rssFeed.addItem(rssItem);
		
		LiveShowTopicsPresenter presenter = new LiveShowTopicsPresenter(this, rssFeed);
		presenter.refreshTopics();
		
		assertEquals(1, topics.size());
	}

	public void setTopics(List<ShowTopic> topics) {
		this.topics = topics;
	}
}
