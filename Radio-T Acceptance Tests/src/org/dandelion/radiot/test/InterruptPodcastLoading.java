package org.dandelion.radiot.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dandelion.radiot.AsyncPresenter;
import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.Factory;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.BasicAcceptanceTestCase;

import android.app.Instrumentation;
import android.content.pm.ActivityInfo;

public class InterruptPodcastLoading extends
		BasicAcceptanceTestCase {

	private ApplicationDriver appDriver;
	private LockedPodcastListFactory factory;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}
	
	@Override
	protected Factory createPodcastListFactory() {
		factory = new LockedPodcastListFactory(getInstrumentation());
		return factory;
	}

	public void testCancelRssLoadingWhenPressingBack() throws Exception {
		appDriver.visitMainShowPage();
		appDriver.goBack();
		try {
			appDriver.assertOnHomeScreen();
		} finally {
			factory.allowPodcastRetrievalFinish();
		}
	}
	
	public void testDestroyingActivityWhileLoading() throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.finish();
		try {
			factory.allowPodcastRetrievalFinish();
			appDriver.assertOnHomeScreen();
		} catch (Exception e) {
			fail("Should not have failed");
		}
	}
	
	public void testChangeOrientation() throws Exception {
		PodcastListActivity activity = appDriver.visitMainShowPage();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		try {
			factory.allowPodcastRetrievalFinish();
			appDriver.waitSomeTime();
		} catch(Exception ex) { 
			fail("The orientation change failed: " + ex.getMessage());
		}
	}
}

class LockedPodcastListFactory extends LocalRssFeedFactory {

	private CountDownLatch modelLatch;

	public LockedPodcastListFactory(Instrumentation instrumentation) {
		super(instrumentation);
		modelLatch = new CountDownLatch(1);
	}
	
	@Override
	public IPresenter createPresenter(IModel model) {
		return new AsyncPresenter(model);
	}
	
	@Override
	public IModel createModel(String url) {
		return new PodcastList.IModel() {
			@Override
			public List<PodcastItem> retrievePodcasts() throws Exception {
				modelLatch.await();
				return new ArrayList<PodcastItem>();
			}
		};
	}
	
	public void allowPodcastRetrievalFinish() {
		modelLatch.countDown();
	}
}