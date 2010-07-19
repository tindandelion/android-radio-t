package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

	private ApplicationDriver appDriver;
	private CountDownLatch podcastRetrievalLatch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
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

	protected IPresenter createTestPresenter(IModel model) {
		return new PodcastListPresenter.AsyncPresenter(model);
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

	public void testCancelRssLoadingWhenPressingBack() throws Exception {
		appDriver.visitMainShowPage();
		appDriver.goBack();
		try {
			appDriver.assertOnHomeScreen();
		} finally {
			allowPodcastRetrievalToFinish();
		}
	}

	protected void allowPodcastRetrievalToFinish() {
		podcastRetrievalLatch.countDown();
	}

	public void testDestroyingActivityWhileLoading() throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.finish();
		try {
			allowPodcastRetrievalToFinish();
			appDriver.assertOnHomeScreen();
		} catch (Exception e) {
			fail("Should not have failed");
		}
	}

	public void testChangeOrientation() throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		try {
			allowPodcastRetrievalToFinish();
			appDriver.waitSomeTime();
		} catch (Exception ex) {
			fail("The orientation change failed: " + ex.getMessage());
		}
	}
}
