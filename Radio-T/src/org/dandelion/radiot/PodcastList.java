package org.dandelion.radiot;

import java.io.IOException;
import java.io.InputStream;
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
	
	public interface IFeedSource {
		InputStream openContentStream() throws IOException;
	}

	public static IPresenter getPresenter(IView view, String feedUrl) {
		Factory f = getFactory();
		IFeedSource feedSource = f.createFeedSource(feedUrl);
		IModel model = f.createModel(feedSource);
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
		public IModel createModel(IFeedSource feedSource) {
			return new RssFeedModel(feedSource);
		}

		public IFeedSource createFeedSource(String url) {
			return new RssFeedModel.UrlFeedSource(url);
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
