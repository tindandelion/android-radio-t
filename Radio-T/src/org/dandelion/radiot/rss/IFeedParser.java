package org.dandelion.radiot.rss;

import java.io.IOException;

import org.xml.sax.SAXException;

public interface IFeedParser {
	public interface ParserListener {
		void onItemParsed(RssItem item);
	}

	void parse(ParserListener listener) throws IOException, SAXException;
}
