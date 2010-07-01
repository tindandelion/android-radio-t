package org.dandelion.radiot;

import java.util.List;

public class PodcastList {
	private static Factory factory;

	public interface IModel {
		List<PodcastItem> retrievePodcasts() throws Exception;
	}

	public interface IView {
		void updatePodcasts(List<PodcastItem> podcasts);

		void showProgress();

		void closeProgress();

		void showErrorMessage(String errorMessage);
	}

	public interface IPresenter {
		void refreshData();
	}

	public static IPresenter getPresenter(IView view) {
		Factory f = getFactory();
		IModel model = f.createModel();
		IPresenter presenter = f.createPresenter(model, view);
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
		private static final String PODCAST_URL = "http://feeds.rucast.net/radio-t";

		public IModel createModel() {
			return new RssFeedModel(new RssFeedModel.UrlFeedSource(PODCAST_URL));
		}

		public IPresenter createPresenter(IModel model, IView view) {
			return new AsyncPresenter(model, view);
		}
	}

	public static IPresenter createSyncPresenter(final IModel model, final IView view) {
		return new IPresenter() {
			public void refreshData() {
				try {
					view.updatePodcasts(model.retrievePodcasts());
				} catch (Exception e) {
				}
			}
		};
	}
}
