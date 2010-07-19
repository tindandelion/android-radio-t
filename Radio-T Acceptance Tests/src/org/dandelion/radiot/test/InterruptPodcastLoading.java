package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.Factory;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastListPresenter;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.BasicAcceptanceTestCase;

import android.content.pm.ActivityInfo;

public class InterruptPodcastLoading extends BasicAcceptanceTestCase {

	private static final int WAIT_TIMEOUT = 5;
	private ApplicationDriver appDriver;
	private CountDownLatch podcastRetrievalLatch;
	private CountDownLatch taskCancelLatch;
	private int numberOfStartedTasks;
	protected int numberOfFinishedTasks;

	public void testCancelRssLoadingWhenPressingBack() throws Exception {
		appDriver.visitMainShowPage();
		appDriver.goBack();
		assertBackgroundTaskIsCancelled();
		appDriver.assertOnHomeScreen();
	}

	public void testChangeOrientationContinuesBackgroundLoading()
			throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		appDriver.waitSomeTime();
		allowPodcastRetrievalToFinish();
		assertNumberOfStartedBackgroundTasks(1);
		appDriver.assertShowingPodcastList();
	}

	private void assertNumberOfStartedBackgroundTasks(int i) {
		assertEquals(i, numberOfStartedTasks);
	}

	public void testDestroyingActivityWhileLoading() throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.finish();
		appDriver.assertOnHomeScreen();
		assertBackgroundTaskIsCancelled();
	}

	protected void allowPodcastRetrievalToFinish() {
		podcastRetrievalLatch.countDown();
	}

	@Override
	protected Factory createPodcastListFactory() {
		return new LocalRssFeedFactory(getInstrumentation()) {
			public IModel createModel(String url) {
				return createTestModel();
			};

			public IPresenter createPresenter(IModel model) {
				return createTestPresenter(model);
			};
		};
	}

	protected IModel createTestModel() {
		podcastRetrievalLatch = new CountDownLatch(1);

		return new PodcastList.IModel() {
			@Override
			public List<PodcastItem> retrievePodcasts() throws Exception {
				podcastRetrievalLatch.await();
				return new ArrayList<PodcastItem>();
			}
		};
	}

	protected IPresenter createTestPresenter(IModel model) {
		taskCancelLatch = new CountDownLatch(1);
		numberOfStartedTasks = 0;
		numberOfFinishedTasks = 0;
		return new PodcastListPresenter.AsyncPresenter(model) {
			@Override
			public void taskStarted() {
				super.taskStarted();
				numberOfStartedTasks += 1;
			}
			
			@Override
			public void taskFinished(UpdateProgress progress) {
				super.taskFinished(progress);
				numberOfFinishedTasks += 1;
			}

			@Override
			public void taskCancelled() {
				super.taskCancelled();
				if (null != taskCancelLatch) {
					taskCancelLatch.countDown();
				}
				numberOfFinishedTasks += 1;
			}
		};
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		if (numberOfStartedTasks > numberOfFinishedTasks) {
			fail("Not all background tasks were finished by tear down");
		}
	}

	private void assertBackgroundTaskIsCancelled() throws InterruptedException {
		if (taskCancelLatch.await(WAIT_TIMEOUT, TimeUnit.SECONDS)) {
			return;
		}
		fail("Failed to wait for task to be cancelled");
	}
}
