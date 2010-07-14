package org.dandelion.radiot.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.dandelion.radiot.AsyncPresenter;
import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastList.IFeedSource;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;
import org.dandelion.radiot.PodcastList.IView;

import com.jayway.android.robotium.solo.Solo;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;

public class BackButtonFunctionality extends
		ActivityInstrumentationTestCase2<HomeScreen> {

	private ApplicationDriver appDriver;
	private LockedPodcastListFactory factory;

	public BackButtonFunctionality() {
		super("org.dandelion.radiot", HomeScreen.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = new LockedPodcastListFactory(getInstrumentation());
		PodcastList.setFactory(factory);
		appDriver = new ApplicationDriver(getInstrumentation(), getActivity());
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

class ApplicationDriver extends Solo {
	public ApplicationDriver(Instrumentation inst, Activity activity) {
		super(inst, activity);
	}

	public void assertOnHomeScreen() {
		assertCurrentActivity("Must be on the home screen", HomeScreen.class);
	}
	
	public void assertOnMainShowPage() {
		assertCurrentActivity("Must be on the main show page", PodcastListActivity.class);
	}

	public void visitMainShowPage() {
		this.clickOnText("Подкасты");
		assertOnMainShowPage();
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