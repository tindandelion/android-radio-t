package org.dandelion.radiot;

import java.util.List;

public class PodcastList {
	public interface IModel {
		List<PodcastItem> retrievePodcasts() throws Exception; 
	}
	
	public interface IView {
		void updatePodcasts(List<PodcastItem> podcasts);
	}
	
	public interface IPresenter { 
		void initialize(IView view);
		void refreshData();
	}

	public static IPresenter nullPresenter() {
		return new IPresenter() {
			public void refreshData() {
			}
			
			public void initialize(IView view) {
			}
		};
	}

	public static IPresenter createPresenter(final IModel model) {
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
				refreshData();
			}
		};
	}
}
