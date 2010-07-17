package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListPresenter;

public class PodcastListPresenterTestCase extends TestCase {
	
	private IModel model;
	private IPresenter presenter;
	protected List<PodcastItem> currentPodcasts;

	public void testCachingPodcastList() throws Exception {
		model = createModel();
		presenter = createPresenter(model);
		presenter.attach(createView());
		
		presenter.refreshData();
		assertNotNull(currentPodcasts);
		List<PodcastItem> originalPodcasts = currentPodcasts;
		
		presenter.refreshData();
		assertTrue(originalPodcasts == currentPodcasts);
	}

	protected PodcastListPresenter createPresenter(IModel model) {
		return new PodcastListPresenter(model) {
			@Override
			public void refreshData() {
				UpdateProgress progress = new UpdateProgress();
				preExecute();
				doInBackground(progress);
				postExecute(progress);
			}
		};
	}

	private IView createView() {
		return new IView() {
			
			public void updatePodcasts(List<PodcastItem> podcasts) {
				currentPodcasts = podcasts;
			}
			
			public void showProgress() {
			}
			
			public void showErrorMessage(String errorMessage) {
			}
			
			public void closeProgress() {
			}
			
			public void close() {
			}
		};
	}

	private IModel createModel() {
		return new IModel() {
			public List<PodcastItem> retrievePodcasts() throws Exception {
				return new ArrayList<PodcastItem>();
			}
		};
	}

}
