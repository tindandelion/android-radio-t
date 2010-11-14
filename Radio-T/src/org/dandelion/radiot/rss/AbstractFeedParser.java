package org.dandelion.radiot.rss;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.util.Xml;

public class AbstractFeedParser implements IFeedParser {
	protected ParserListener listener;
	protected IFeedSource feedSource;
	
	public AbstractFeedParser(IFeedSource source) { 
		feedSource = source;
	}

	public void setListener(ParserListener listener) {
		this.listener = listener;
	}

	public void parse() throws IOException, SAXException {
		Xml.parse(feedSource.openFeedStream(), Xml.Encoding.UTF_8,
				getContentHandler());
	}

	protected ContentHandler getContentHandler() {
		return null;
	}
	
	protected void fireNewItem(RssItem item) { 
		if (null != listener) {
			listener.onItemParsed(item);
		}
	}
}