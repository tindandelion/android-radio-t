package org.dandelion.radiot.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.dandelion.radiot.podcasts.core.NullThumbnailDownloader;
import org.dandelion.radiot.podcasts.core.PodcastListLoader;
import org.dandelion.radiot.podcasts.core.PodcastsProvider;
import org.dandelion.radiot.podcasts.core.RssFeedProvider;

import android.content.res.AssetManager;

public class PodcastListAcceptanceTestCase extends BasicAcceptanceTestCase {
	protected ArrayList<TestLoader> loaders;


	@Override
	protected PodcastsProvider createTestModel(final String url) {
		final AssetManager assets = getInstrumentation().getContext()
				.getAssets();
		return new RssFeedProvider(url, new NullThumbnailDownloader()) {
			@Override
			protected InputStream openContentStream() throws IOException {
				return assets.open((url + ".xml"));
			}
		};
	}

	@Override
	protected PodcastListLoader createTestEngine(PodcastsProvider model) {
		TestLoader loader = new TestLoader(model);
		loaders.add(loader);
		return loader;
	}
	
	public TestLoader mainShowPresenter() {
		return loaders.get(0);
	}
	
	public TestLoader afterShowPresenter() {
		return loaders.get(1);
	}
	
	@Override
	protected void setUp() throws Exception {
		loaders = new ArrayList<TestLoader>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		for (TestLoader loader : loaders) {
			loader.assertNoTasksAreActive();
		}
	}

}