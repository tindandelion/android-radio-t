package org.dandelion.radiot.test;

import org.dandelion.radiot.HomeScreen;
import org.dandelion.radiot.PodcastList;
import org.dandelion.radiot.PodcastList.IView;
import org.dandelion.radiot.PodcastListActivity;
import org.dandelion.radiot.PodcastList.IModel;
import org.dandelion.radiot.PodcastList.IPresenter;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class HomeScreenTestCase extends
		ActivityInstrumentationTestCase2<HomeScreen> {

	private Solo solo;

	public HomeScreenTestCase() {
		super("org.dandelion.radiot", HomeScreen.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PodcastList.setFactory(new PodcastList.Factory() {
			@Override
			public IPresenter createPresenter(IModel model, IView view) {
				return PodcastList.createSyncPresenter(model, view);
			}
		});
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testOpenPodcastsPage() throws Exception {
		solo.clickOnText("Подкасты");
		solo.assertCurrentActivity("Main podcast list is not shown",
				PodcastListActivity.class);
	}

	public void testShowAfterShowPage() throws Exception {
		solo.clickOnText("После-шоу");
		assertTrue("The sample podcast record for pirates is not found",
				solo.waitForText("#10193"));
	}
}
