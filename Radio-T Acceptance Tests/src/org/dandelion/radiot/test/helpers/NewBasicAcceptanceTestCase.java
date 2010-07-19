package org.dandelion.radiot.test.helpers;

import java.io.IOException;
import java.io.InputStream;

import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.RssFeedModel;

import android.content.res.AssetManager;
import android.net.Uri;

public class NewBasicAcceptanceTestCase extends BasicAcceptanceTestCase {
	protected TestPresenter testPresenter;

	@Override
	protected PodcastList.Factory createPodcastListFactory() {
		return new PodcastList.Factory() {
			public IModel createModel(String url) {
				return createTestModel(url);
			};

			public IPresenter createPresenter(IModel model) {
				return createTestPresenter(model);
			};
		};
	}

	protected IModel createTestModel(final String url) {
		final AssetManager assets = getInstrumentation().getContext()
				.getAssets();
		return new RssFeedModel(url) {
			@Override
			protected InputStream openContentStream() throws IOException {
				return assets.open(getLocalFileName(url));
			}

			private String getLocalFileName(String url) {
				String filename;
				if (null == url) {
					filename = "radio-t";
				} else {
					filename = Uri.parse(url).getLastPathSegment();
				}
				return filename + ".xml";
			}

			@Override
			protected InputStream openImageStream(String address) {
				return null;
			}
		};
	}

	protected IPresenter createTestPresenter(IModel model) {
		testPresenter = new TestPresenter(model);
		return testPresenter;
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		if (null != testPresenter) {
			testPresenter.assertNoTasksAreActive();
		}
	}

}