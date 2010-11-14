package org.dandelion.radiot.unittest;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.RssFeedModel;
import org.dandelion.radiot.rss.RssFeedParser;
import org.dandelion.radiot.rss.RssItem;
import org.xml.sax.SAXException;

class FakeRssParser extends RssFeedParser {

	public RssItem sampleItem;

	public FakeRssParser() {
		super(null);
		sampleItem = new RssItem();
	}
	
	@Override
	public void parse() throws IOException, SAXException {
		listener.onItemParsed(sampleItem);
	}
	
}
public class RssFeedModelTestCase extends TestCase {

	private IModel model;
	protected String requestedImageUrl;
	protected RssFeedParser rssParser;
	private FakeRssParser fakeParser;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		fakeParser = new FakeRssParser();
		model = new RssFeedModel(fakeParser);
	}
	
	public void testCreatesAndReturnsAListOfPodcastItems() throws Exception {
		fakeParser.sampleItem.title = "Radio-T 100";
		List<PodcastItem> podcastItems = model.retrievePodcasts();
		assertEquals(1, podcastItems.size());
	}
}
