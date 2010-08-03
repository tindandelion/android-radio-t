package org.dandelion.radiot;

import java.util.List;

import android.graphics.Bitmap;

public class PodcastList {
	private static Factory factory;

	public interface IModel {
		List<PodcastItem> retrievePodcasts() throws Exception;
		Bitmap loadPodcastImage(PodcastItem item);
	}

	public interface IView {
		void updatePodcasts(List<PodcastItem> podcasts);
		void showProgress();
		void closeProgress();
		void showErrorMessage(String errorMessage);
		void updatePodcastImage(int index);
	}

	public interface IPodcastListEngine {
		void refresh(boolean resetCache);
		void cancelUpdate();
		void detach();
		void attach(IView view);
	}

	public static IPodcastListEngine getPresenter(String feedUrl) {
		Factory f = getFactory();
		IModel model = f.createModel(feedUrl);
		IPodcastListEngine presenter = f.createPresenter(model);
		return presenter;
	}

	private static Factory getFactory() {
		if (null == factory) {
			factory = new Factory();
		}
		return factory;
	}

	public static void setFactory(Factory newInstance) {
		factory = newInstance;
	}

	public static void resetFactory() {
		setFactory(null);
	}

	public static class Factory {
		public IModel createModel(String url) {
			return new RssFeedModel(url);
		}

		public IPodcastListEngine createPresenter(IModel model) {
			return new PodcastListEngine(model);
		}
	}
}
