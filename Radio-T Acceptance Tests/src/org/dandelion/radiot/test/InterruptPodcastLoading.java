package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.NewBasicAcceptanceTestCase;

import android.content.pm.ActivityInfo;

public class InterruptPodcastLoading extends NewBasicAcceptanceTestCase {

	private ApplicationDriver appDriver;
	protected CountDownLatch podcastRetrievalLatch;

	public void testCancelRssLoadingWhenPressingBack() throws Exception {
		appDriver.visitMainShowPage();
		appDriver.goBack();
		testPresenter.assertTaskIsCancelled();
		appDriver.assertOnHomeScreen();
	}

	public void testChangeOrientationContinuesBackgroundLoading()
			throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		appDriver.waitSomeTime();
		allowPodcastRetrievalToFinish();
		testPresenter.assertStartedBackgroundTasksCount(1);
		appDriver.assertShowingPodcastList();
	}

	public void testDestroyingActivityWhileLoading() throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.finish();
		appDriver.assertOnHomeScreen();
		testPresenter.assertTaskIsCancelled();
	}

	protected void allowPodcastRetrievalToFinish() {
		podcastRetrievalLatch.countDown();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}


	protected IModel createTestModel(String url) {
		podcastRetrievalLatch = new CountDownLatch(1);
	
		return new PodcastList.IModel() {
			@Override
			public List<PodcastItem> retrievePodcasts() throws Exception {
				podcastRetrievalLatch.await();
				return new ArrayList<PodcastItem>();
			}
		};
	}

}
