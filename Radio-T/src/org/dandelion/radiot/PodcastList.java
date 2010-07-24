package org.dandelion.radiot;

import java.util.List;

public class PodcastList {
	private static Factory factory;

	public interface IModel {
		List<PodcastItem> retrievePodcasts() throws Exception;

		public abstract void loadPodcastImage(PodcastItem item);
	}

	public interface IView {
		void updatePodcasts(List<PodcastItem> podcasts);
		void showProgress();
		void closeProgress();
		void showErrorMessage(String errorMessage);
	}

	public interface IPresenter {
		void refreshData(boolean resetCache);
		void cancelUpdate();
		void detach();
		void attach(IView view);
	}

	public static IPresenter getPresenter(IView view, String feedUrl) {
		Factory f = getFactory();
		IModel model = f.createModel(feedUrl);
		IPresenter presenter = f.createPresenter(model);
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

		public IPresenter createPresenter(IModel model) {
			return new PodcastListPresenter(model);
		}
	}
}
