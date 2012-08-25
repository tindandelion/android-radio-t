package org.dandelion.radiot.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.dandelion.radiot.podcasts.core.NullThumbnailDownloader;
import org.dandelion.radiot.podcasts.core.PodcastList.IModel;
import org.dandelion.radiot.podcasts.core.PodcastList.IPodcastListEngine;
import org.dandelion.radiot.podcasts.core.RssFeedModel;

import android.content.res.AssetManager;

public class PodcastListAcceptanceTestCase extends BasicAcceptanceTestCase {
	protected ArrayList<TestPresenter> presenters;


	@Override
	protected IModel createTestModel(final String url) {
		final AssetManager assets = getInstrumentation().getContext()
				.getAssets();
		return new RssFeedModel(url, new NullThumbnailDownloader()) {
			@Override
			protected InputStream openContentStream() throws IOException {
				return assets.open((url + ".xml"));
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