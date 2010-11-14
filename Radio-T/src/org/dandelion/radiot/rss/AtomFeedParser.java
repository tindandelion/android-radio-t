package org.dandelion.radiot.rss;

import org.xml.sax.ContentHandler;

import android.sax.RootElement;

public class AtomFeedParser extends AbstractFeedParser {

	public AtomFeedParser(IFeedSource source) {
		super(source);
	}
	
	@Override
	protected ContentHandler getContentHandler() {
		RootElement root = new RootElement("http://www.w3.org/2005/Atom", "feed");
		return root.getContentHandler();
	}

	public static IFeedParser withRemoteFeed(String url) {
		return new AtomFeedParser(new RemoteFeedSource(url));
	}

}
