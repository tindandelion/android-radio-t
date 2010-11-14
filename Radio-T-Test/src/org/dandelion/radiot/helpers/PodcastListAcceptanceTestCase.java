package org.dandelion.radiot.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.RssFeedModel;
import org.dandelion.radiot.rss.IFeedSource;
import org.dandelion.radiot.rss.RssFeedParser;

import android.content.res.AssetManager;

public class PodcastListAcceptanceTestCase extends BasicAcceptanceTestCase {
	protected ArrayList<TestPresenter> presenters;


	@Override
	protected IModel createTestModel(final String url) {
		final AssetManager assets = getInstrumentation().getContext()
				.getAssets();
		
		RssFeedParser rssParser = new RssFeedParser(new IFeedSource() {
			public InputStream openFeedStream() throws IOException {
				return assets.open((url + ".xml"));
				}
		});
		
		return new RssFeedModel(rssParser) {
			@Override
			protected InputStream openImageStream(String address) {
				return null;
			}
		};
	}

	@Override
	protected IPodcastListEngine createTestEngine(IModel model) {
		TestPresenter presenter = new TestPresenter(model);
		presenters.add(presenter);
		return presenter;
	}
	
	public TestPresenter mainShowPresenter() { 
		return presenters.get(0);
	}
	
	public TestPresenter afterShowPresenter() {
		return presenters.get(1);
	}
	
	@Override
	protected void setUp() throws Exception {
		presenters = new ArrayList<TestPresenter>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		for (TestPresenter presenter : presenters) {
			presenter.assertNoTasksAreActive();
		}
	}

}