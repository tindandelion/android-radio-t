package org.dandelion.radiot.rss;

import java.io.IOException;

import org.dandelion.radiot.rss.AsyncFeedParser.ProgressListener;
import org.dandelion.radiot.rss.IFeedParser.ParserListener;
import org.xml.sax.SAXException;

import android.os.AsyncTask;

public class AsyncFeedParser implements IFeedParser {
	public interface ProgressListener {
		void onStartedReading();

		void onFinishedReading();
	}

	private IFeedParser parser;
	private ProgressListener progressListener;

	public AsyncFeedParser(IFeedParser realParser,
			ProgressListener progressListener) {
		this.parser = realParser;
		this.progressListener = progressListener;
	}

	public void parse(ParserListener parserListener) throws IOException,
			SAXException {
		new ParseTask(parser, parserListener, progressListener).execute();
	}

}

class ParseTask extends AsyncTask<Void, RssItem, Void> {
	private IFeedParser parser;
	private ProgressListener progressListener;
	private ParserListener realParserListener;
	private ParserListener listener = new ParserListener() {
		public void onItemParsed(RssItem item) {
			publishProgress(item);
		}
	};

	public ParseTask(IFeedParser parser, ParserListener realListener,
			ProgressListener progressListener) {
		this.parser = parser;
		this.realParserListener = realListener;
		this.progressListener = progressListener;
	}
	
	@Override
	protected void onPreExecute() {
		if (null != progressListener) {
			progressListener.onStartedReading();
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (null != progressListener) {
			progressListener.onFinishedReading();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			parser.parse(listener);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(RssItem... values) {
		realParserListener.onItemParsed(values[0]);
	}

}
