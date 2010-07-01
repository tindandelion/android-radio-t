package org.dandelion.radiot;

import java.util.List;

public class PodcastList {
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

	public static class Factory {
		private static final String PODCAST_URL = "http://feeds.rucast.net/radio-t";
		private static Factory instance;
	
		public static Factory getInstance() {
			if (null == instance) {
				instance = new Factory();
			}
			return instance;
		}
		
		public static void setInstance(Factory newInstance) {
			instance = newInstance;
		}
	
		private IPresenter presenter;
	
		public PodcastList.IPresenter getPresenter() {
			if (null == presenter) {
				IModel model = new RssFeedModel(
						new RssFeedModel.UrlFeedSource(PODCAST_URL));
				presenter = createPresenter(model);
			}
			return presenter;
		}

		public IPresenter createPresenter(IModel model) {
			return PodcastList.createAsyncPresenter(model);
		}
		
		public void resetPresenter() {
			presenter = null;
		}
	}

	public static IPresenter nullPresenter() {
		return new IPresenter() {
			public void refreshData() {
			}
			
			public void initialize(IView view) {
			}
		};
	}
	
	public static IPresenter createAsyncPresenter(IModel model) {
		return new AsyncPresenter(model);
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
