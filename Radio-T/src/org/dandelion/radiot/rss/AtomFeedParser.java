package org.dandelion.radiot.rss;

import org.xml.sax.ContentHandler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;

public class AtomFeedParser extends AbstractFeedParser {
	private static final String NAMESPACE = "http://www.w3.org/2005/Atom";
	private RssItem currentItem = new RssItem();

	public AtomFeedParser(IFeedSource source) {
		super(source);
	}

	@Override
	protected ContentHandler getContentHandler() {
		RootElement root = new RootElement(NAMESPACE, "feed");
		Element entry = root.getChild(NAMESPACE, "entry");
		entry.setEndElementListener(new EndElementListener() {
			public void end() {
				fireNewItem(currentItem);
				currentItem = new RssItem();
			}
		});

		entry.getChild(NAMESPACE, "title").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentItem.title = body;
					}
				});

		return root.getContentHandler();
	}

	public static IFeedParser withRemoteFeed(String url) {
		return new AtomFeedParser(new RemoteFeedSource(url));
	}

}
