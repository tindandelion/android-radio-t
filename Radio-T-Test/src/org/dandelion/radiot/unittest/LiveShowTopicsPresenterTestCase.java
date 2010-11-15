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
	private LiveShowTopicsPresenter presenter;
	private FakeRssFeed rssFeed;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		topics = new ArrayList<ShowTopic>();
		rssFeed = new FakeRssFeed();
		presenter = new LiveShowTopicsPresenter(this, rssFeed);
	}
	
	public void testClearsTopicsListWhenAskedToRefresh() throws Exception {
		topics.add(new ShowTopic("Old topic"));
		
		presenter.refreshTopics();
		
		assertEquals(0, topics.size());
	}
	
	public void testCreateShowTopicListFromRss() throws Exception {
		RssItem rssItem = new RssItem("Live show topic");
		rssFeed.addItem(rssItem);
		
		presenter.refreshTopics();
		
		assertEquals(1, topics.size());
	}

	public void clearTopics() {
		topics.clear();
	}

	public void addTopic(ShowTopic topic) {
		topics.add(topic);
	}
}
