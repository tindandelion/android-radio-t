package org.dandelion.radiot.accepttest;

import org.dandelion.radiot.accepttest.drivers.HomeScreenDriver;
import org.dandelion.radiot.podcasts.core.PodcastList.IModel;
import org.dandelion.radiot.podcasts.ui.PodcastListActivity;
import org.dandelion.radiot.helpers.PodcastListAcceptanceTestCase;
import org.dandelion.radiot.helpers.TestModel;

import android.content.pm.ActivityInfo;

public class InterruptPodcastLoading extends PodcastListAcceptanceTestCase {

	private HomeScreenDriver driver;
	private TestModel model;

	public void testChangeOrientationContinuesBackgroundLoading()
			throws Exception {
		PodcastListActivity activity = driver.visitMainShowPage();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		driver.waitSomeTime();
		allowPodcastRetrievalToFinish();
		mainShowPresenter().assertStartedBackgroundTasksCount(1);
		driver.assertShowingPodcastList();
	}

	public void testDestroyingActivityWhileLoading() throws Exception {
		PodcastListActivity activity = driver.visitMainShowPage();
		activity.finish();
		driver.assertOnHomeScreen();
		mainShowPresenter().assertTaskIsCancelled();
	}

	protected void allowPodcastRetrievalToFinish() {
		model.returnsEmptyPodcastList();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		driver = createDriver();
	}

    @Override
    protected void tearDown() throws Exception {
        driver.finish();
        super.tearDown();
    }

    protected IModel createTestModel(String url) {
		model = new TestModel();
		return model;
	}
}
