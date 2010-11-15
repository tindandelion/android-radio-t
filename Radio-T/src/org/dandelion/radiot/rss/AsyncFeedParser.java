package org.dandelion.radiot.rss;

import java.io.IOException;

import org.dandelion.radiot.rss.IFeedParser.ParserListener;
import org.xml.sax.SAXException;

import android.os.AsyncTask;

public class AsyncFeedParser implements IFeedParser {

	private IFeedParser parser;

	public AsyncFeedParser(IFeedParser realParser) {
		this.parser = realParser;
	}

	public void parse(ParserListener listener) throws IOException, SAXException {
		new ParseTask(parser, listener).execute();
	}

}

class ParseTask extends AsyncTask<Void, RssItem, Void> {
	private IFeedParser realParser;
	private ParserListener realListener;
	private ParserListener listener = new ParserListener() {
		public void onItemParsed(RssItem item) {
			publishProgress(item);
		}
	};

	public ParseTask(IFeedParser parser, ParserListener realListener) {
		this.realParser = parser;
		this.realListener = realListener;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			realParser.parse(listener);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(RssItem... values) {
		realListener.onItemParsed(values[0]);
	}

}
