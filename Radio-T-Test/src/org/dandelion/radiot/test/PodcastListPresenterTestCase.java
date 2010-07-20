package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListPresenter;

public class PodcastListPresenterTestCase extends TestCase implements IModel {

	protected List<PodcastItem> displayedPodcasts;
	private Exception errorToThrow;
	private List<PodcastItem> podcastsFromModel;
	private IPresenter presenter;

	public List<PodcastItem> retrievePodcasts() throws Exception {
		if (null != errorToThrow) {
			throw errorToThrow;
		}
		return podcastsFromModel;
	}

	public void testCachingPodcastList() throws Exception {
		ArrayList<PodcastItem> firstPodcasts = newPodcastList();
		ArrayList<PodcastItem> secondPodcasts = newPodcastList();

		modelReturnsPodcasts(firstPodcasts);
		presenter.refreshData(false);
		assertDisplaysPodcasts(firstPodcasts);

		modelReturnsPodcasts(secondPodcasts);
		presenter.refreshData(false);
		assertDisplaysPodcasts(firstPodcasts);
	}

	public void testDoNotCacheErrorResult() throws Exception {
		modelThrowsError();
		presenter.refreshData(false);
		assertDisplaysPodcasts(null);

		ArrayList<PodcastItem> podcasts = newPodcastList();
		modelReturnsPodcasts(podcasts);
		presenter.refreshData(false);
		assertDisplaysPodcasts(podcasts);
	}

	public void testForceClearCache() throws Exception {
		ArrayList<PodcastItem> firstPodcasts = newPodcastList();
		ArrayList<PodcastItem> secondPodcasts = newPodcastList();

		modelReturnsPodcasts(firstPodcasts);
		presenter.refreshData(false);
		assertDisplaysPodcasts(firstPodcasts);

		modelReturnsPodcasts(secondPodcasts);
		presenter.refreshData(true);
		assertDisplaysPodcasts(secondPodcasts);
	}

	protected PodcastListPresenter createPresenter(IModel model) {
		return new PodcastListPresenter(model) {
			@Override
			protected void forkWorkerThread() {
				UpdateProgress progress = new UpdateProgress();
				taskStarted();
				doInBackground(progress);
				taskFinished(progress);
			}
		};
	}

	protected ArrayList<PodcastItem> newPodcastList() {
		return new ArrayList<PodcastItem>();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		presenter = createPresenter(this);
		presenter.attach(createView());
	}

	private void assertDisplaysPodcasts(ArrayList<PodcastItem> expectedList) {
		assertTrue(expectedList == displayedPodcasts);
	}

	private IView createView() {
		return new IView() {

			public void closeProgress() {
			}

			public void showErrorMessage(String errorMessage) {
			}

			public void showProgress() {
			}

			public void updatePodcasts(List<PodcastItem> podcasts) {
				displayedPodcasts = podcasts;
			}
		};
	}

	private void modelReturnsPodcasts(ArrayList<PodcastItem> list) {
		errorToThrow = null;
		podcastsFromModel = list;
	}

	private void modelThrowsError() {
		errorToThrow = new Exception();
	}

}
