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
		void initialize(IView view);

		void refreshData();
	}
	
	public static IPresenter getPresenter(IView view) {
		
		return getFactory().getPresenter(view);
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
		private IPresenter presenter;

		public PodcastList.IPresenter getPresenter(IView view) {
			if (null == presenter) {
				IModel model = createModel();
				presenter = createPresenter(model);
			}
			presenter.initialize(view);
			return presenter;
		}

		public IModel createModel() {
			return new RssFeedModel(new RssFeedModel.UrlFeedSource(PODCAST_URL));
		}

		public IPresenter createPresenter(IModel model) {
			return new AsyncPresenter(model);
		}
	}

	public static IPresenter createSyncPresenter(final IModel model) {
		return new IPresenter() {
			private IView view;

			public void refreshData() {
				try {
					view.updatePodcasts(model.retrievePodcasts());
				} catch (Exception e) {
				}
			}

			public void initialize(IView view) {
				this.view = view;
			}
		};
	}
}
