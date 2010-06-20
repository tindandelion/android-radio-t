package org.dandelion.radiot;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dandelion.radiot.PodcastListActivity.IPodcastProvider;
import org.dandelion.radiot.PodcastListActivity.PodcastListAdapter;
import org.xml.sax.helpers.DefaultHandler;

import android.net.Uri;

public class RssPodcastProvider implements IPodcastProvider {
	public class RssHandler extends DefaultHandler {

	}

	private Uri feedUri;

	public RssPodcastProvider(Uri feedUri) {
		this.feedUri = feedUri;
	}

	public void retrievePodcasts(PodcastListAdapter listAdapter) {
		ArrayList<PodcastInfo> list = new ArrayList<PodcastInfo>();
		try {
			parseFeed(list);
		} catch (Exception e) {
		}
		
		for (PodcastInfo info : list) {
			listAdapter.add(info);
		}
	}

	public void parseFeed(ArrayList<PodcastInfo> list) throws Exception {
		SAXParser parser = createParser();
		RssHandler handler = new RssHandler();
		parser.parse(feedUri.toString(), handler);
	}

	private SAXParser createParser() throws Exception {
		return SAXParserFactory.newInstance().newSAXParser();
	}
}
