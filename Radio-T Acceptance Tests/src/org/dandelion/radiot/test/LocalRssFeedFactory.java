package org.dandelion.radiot.test;

import java.io.IOException;
import java.io.InputStream;

import org.dandelion.radiot.PodcastListPresenter;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.RssFeedModel;

import android.app.Instrumentation;
import android.content.res.AssetManager;
import android.net.Uri;

class LocalRssFeedFactory extends PodcastList.Factory {
	private AssetManager assets;

	public LocalRssFeedFactory(Instrumentation instrumentation) {
		assets = instrumentation.getContext().getAssets();
	}

	@Override
	public IPresenter createPresenter(final IModel model) {
		return new PodcastListPresenter.SyncPresenter(model);
	}

	@Override
	public IModel createModel(final String url) {
		return new RssFeedModel(url) {
			@Override
			protected InputStream openContentStream() throws IOException {
				return assets.open(getLocalFileName(url));
			}

			@Override
			protected InputStream openImageStream(String address) {
				return null;
			}
		};
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
}
