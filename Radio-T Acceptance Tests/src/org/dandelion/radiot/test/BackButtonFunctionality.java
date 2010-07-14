package org.dandelion.radiot.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dandelion.radiot.AsyncPresenter;
import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IFeedSource;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.test.helpers.ApplicationDriver;
import org.dandelion.radiot.test.helpers.BasicAcceptanceTestCase;

import android.app.Instrumentation;

public class BackButtonFunctionality extends
		BasicAcceptanceTestCase {

	private ApplicationDriver appDriver;
	private LockedPodcastListFactory factory;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		appDriver = createApplicationDriver();
	}
	
	@Override
	protected void tweakPodcastListFactory() {
		factory = new LockedPodcastListFactory(getInstrumentation());
		PodcastList.setFactory(factory);
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
}

class LockedPodcastListFactory extends LocalRssFeedFactory {

	private CountDownLatch modelLatch;

	public LockedPodcastListFactory(Instrumentation instrumentation) {
		super(instrumentation);
		modelLatch = new CountDownLatch(1);
	}
	
	@Override
	public IPresenter createPresenter(IModel model, IView view) {
		return new AsyncPresenter(model, view);
	}
	
	@Override
	public IModel createModel(IFeedSource feedSource) {
		final IModel model = super.createModel(feedSource);
		return new PodcastList.IModel() {
			@Override
			public List<PodcastItem> retrievePodcasts() throws Exception {
				List<PodcastItem> result = model.retrievePodcasts();
				modelLatch.await();
				return result;
			}
		};
	}
	
	public void allowPodcastRetrievalFinish() {
		modelLatch.countDown();
	}
}