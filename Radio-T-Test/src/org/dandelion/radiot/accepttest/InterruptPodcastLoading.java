package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.accepttest.drivers.ApplicationDriver;
import org.dandelion.radiot.podcasts.core.PodcastList.IModel;
import org.dandelion.radiot.podcasts.ui.ListActivity;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.helpers.TestModel;

import android.content.pm.ActivityInfo;

public class InterruptPodcastLoading extends PodcastListAcceptanceTestCase {

	private ApplicationDriver appDriver;
	private TestModel model;

	public void testCancelRssLoadingWhenPressingBack() throws Exception {
		appDriver.visitMainShowPage();
		appDriver.goBack();
		mainShowPresenter().assertTaskIsCancelled();
		appDriver.assertOnHomeScreen();
	}

	public void testChangeOrientationContinuesBackgroundLoading()
			throws Exception {
		ListActivity activity = appDriver.visitMainShowPage();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		appDriver.waitSomeTime();
		allowPodcastRetrievalToFinish();
		mainShowPresenter().assertStartedBackgroundTasksCount(1);
		appDriver.assertShowingPodcastList();
	}

	public void testDestroyingActivityWhileLoading() throws Exception {
		ListActivity activity = appDriver.visitMainShowPage();
		activity.finish();
		appDriver.assertOnHomeScreen();
		mainShowPresenter().assertTaskIsCancelled();
	}

	protected void allowPodcastRetrievalToFinish() {
		model.returnsEmptyPodcastList();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}

    @Override
    protected void tearDown() throws Exception {
        appDriver.finish();
        super.tearDown();
    }

    protected IModel createTestModel(String url) {
		model = new TestModel();
		return model;
	}
}
